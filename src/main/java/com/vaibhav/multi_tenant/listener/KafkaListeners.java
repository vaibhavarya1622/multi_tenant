package com.vaibhav.multi_tenant.listener;

import com.vaibhav.multi_tenant.constant.AppConstants;
import com.vaibhav.multi_tenant.services.PpoService;
import com.vaibhav.multi_tenant.tenant.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Slf4j
public class KafkaListeners {
    @Autowired
    private PpoService ppoService;
    @KafkaListener(topics = "${spring.kafka.consumer.topic.dbOperator}",groupId = "${spring.kafka.consumer.group-id}")
    public void listener(final String payload, @Header(AppConstants.Tenant)String tenantId) throws IOException{
        ppoService.saveData(tenantId,payload);
        TenantContext.remove();
    }
}
