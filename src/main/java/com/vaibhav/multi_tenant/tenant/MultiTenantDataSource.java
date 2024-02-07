package com.vaibhav.multi_tenant.tenant;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultiTenantDataSource extends AbstractRoutingDataSource {
    @Override
    protected String determineCurrentLookupKey(){
        return TenantContext.getCurrentTenant();
    }
}
