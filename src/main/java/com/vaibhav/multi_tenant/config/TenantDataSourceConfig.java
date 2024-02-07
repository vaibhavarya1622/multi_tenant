package com.vaibhav.multi_tenant.config;

import com.vaibhav.multi_tenant.model.admin.Config;
import com.vaibhav.multi_tenant.repository.admin.ConfigRepository;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.vaibhav.multi_tenant.repository.tenant"},
        entityManagerFactoryRef = "tenantManagerFactory",
        transactionManagerRef = "tenantTransactionManager"
)
public class TenantDataSourceConfig {
    @Autowired
    private ConfigRepository configRepository;
    @Value("${dbOperator.batchSize}")
    private int batchSize;
    @Bean("tenantManagerFactory")
    LocalContainerEntityManagerFactoryBean tenantManger(@Qualifier("tenantDataSource") DataSource tenantDataSource){
        return entityManager(ProxyDataSourceBuilder.create(tenantDataSource).name("tenantDataSource").asJson().countQuery().logQueryToSysOut().build(),"com.vaibhav.multi_tenant.model.tenant");
    }
    @Bean("tenantTransactionManager")
    PlatformTransactionManager tenantTransactionManager(@Qualifier("tenantManagerFactory") LocalContainerEntityManagerFactoryBean tenantEntityManager){
        return getTransactionManager(tenantEntityManager);
    }
    private LocalContainerEntityManagerFactoryBean entityManager(DataSource dataSource, String... packagesToScan){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(packagesToScan);
        em.setPersistenceUnitName("tenant-entity-manager");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        return em;
    }
    private PlatformTransactionManager getTransactionManager(LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
        return transactionManager;
    }
}
