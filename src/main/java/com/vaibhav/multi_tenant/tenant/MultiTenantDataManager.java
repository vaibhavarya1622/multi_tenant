package com.vaibhav.multi_tenant.tenant;

import com.vaibhav.multi_tenant.model.admin.Config;
import com.vaibhav.multi_tenant.repository.admin.ConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MultiTenantDataManager {
    public final Map<Object,Object> tenantDataSources = new ConcurrentHashMap<>();
    @Bean("tenantDataSource")
    public DataSource dataSource(@Qualifier("configRepository")ConfigRepository configRepository) throws SQLException{
        AbstractRoutingDataSource multiTenantDataSource = new MultiTenantDataSource();
        List<Config> tenants = configRepository.findAll();
        if(tenants.isEmpty()) {
            String msg = "No tenant is found";
            log.error(msg);
        }
        for(Config tenant:tenants) {
            DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create()
                    .url(tenant.getUrl())
                    .password(tenant.getPassword())
                    .username(tenant.getUserName())
                    .driverClassName(tenant.getDriverClassName());
            //Check if new connection is alive.
            try(Connection connection = dataSourceBuilder.build().getConnection()){
                tenantDataSources.put(tenant.getName(),dataSourceBuilder.build());
                log.debug(":::::Tenant added: {}",tenant.getName());
            }
        }
        multiTenantDataSource.setDefaultTargetDataSource(defaultDataSource());
        multiTenantDataSource.setTargetDataSources(tenantDataSources);
        multiTenantDataSource.afterPropertiesSet();;
        return multiTenantDataSource;
    }
    private DriverManagerDataSource defaultDataSource(){
        DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
        defaultDataSource.setDriverClassName("org.h2.Driver");
        defaultDataSource.setUrl("jdbc:h2:mem:default");
        defaultDataSource.setUsername("default");
        defaultDataSource.setPassword("default");
        return defaultDataSource;
    }
}
