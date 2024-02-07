package com.vaibhav.multi_tenant.model.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
public class Config {
    @Id
    @Column(columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Transient
    @JsonIgnore
    public boolean isNew(){
        return getId() == null;
    }
    @Column
    private String name;
    @Column
    private String url;
    @Column
    private String userName;
    @Column
    private String password;
    @Column
    private String driverClassName;
    @Column
    private String dialect;
}
