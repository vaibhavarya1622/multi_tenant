package com.vaibhav.multi_tenant.services;

import com.vaibhav.multi_tenant.Exception.TenantNotFoundException;
import com.vaibhav.multi_tenant.constant.AppConstants;
import com.vaibhav.multi_tenant.model.admin.Config;
import com.vaibhav.multi_tenant.repository.admin.ConfigRepository;
import com.vaibhav.multi_tenant.tenant.TenantContext;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class KafkaListenerInterceptor<K,V> implements RecordInterceptor<K,V> {
    @Autowired
    private ConfigRepository configRepository;

    @Override
    public ConsumerRecord<K, V> intercept(ConsumerRecord<K, V> record, Consumer<K, V> consumer) {
        Headers headers = record.headers();
        for(Header header:headers){
            if(header.key().equals(AppConstants.Tenant)){
                String tenantId = new String(header.value(), StandardCharsets.UTF_8);
                try{
                    Config tenants = configRepository.findByName(tenantId);
                }
                catch (NoResultException e){
                    throw new TenantNotFoundException(tenantId+" not a valid tenant");
                }
                TenantContext.setCurrentTenant(tenantId);
            }
        }
        return record;
    }
}
