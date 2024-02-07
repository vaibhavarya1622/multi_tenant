package com.vaibhav.multi_tenant.tenant;

import org.springframework.stereotype.Component;

@Component
public class TenantContext {
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();
    public static String getCurrentTenant(){
        return CURRENT_TENANT.get();
    }
    public static void setCurrentTenant(String currentTenant){
        CURRENT_TENANT.set(currentTenant);
    }
    public static void remove(){
        CURRENT_TENANT.remove();
    }
}
