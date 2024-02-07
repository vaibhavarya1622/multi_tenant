package com.vaibhav.multi_tenant.repository.tenant;

import com.vaibhav.multi_tenant.model.tenant.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
