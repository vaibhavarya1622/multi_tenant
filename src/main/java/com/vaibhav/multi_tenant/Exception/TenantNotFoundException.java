package com.vaibhav.multi_tenant.Exception;

import jakarta.persistence.NoResultException;

public class TenantNotFoundException extends NoResultException {
    public TenantNotFoundException(String msg){
        super(msg);
    }
}
