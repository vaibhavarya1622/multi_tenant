package com.vaibhav.multi_tenant.repository.admin;

import com.vaibhav.multi_tenant.model.admin.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<Config,Long> {
    Config findByName(String name);
}

