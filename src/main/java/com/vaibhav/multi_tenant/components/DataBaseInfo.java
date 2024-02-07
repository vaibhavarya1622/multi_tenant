package com.vaibhav.multi_tenant.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class DataBaseInfo {
    private final DataSource dataSource;
    public DataBaseInfo(@Qualifier("tenantDataSource") DataSource dataSource){
        this.dataSource = dataSource;
    }
    public void printDatabaseInfo() throws SQLException{
        String url = dataSource.getConnection().getMetaData().getURL();
        String username = dataSource.getConnection().getMetaData().getUserName();
        String driverName = dataSource.getConnection().getMetaData().getDriverName();
        log.debug("::::Database Url:{} ::::Username: {} :::: driverName:{}",url,username,driverName);

        String[] tableTypes = {"TABLE"};
        String schema = "npodb";
        ResultSet resultSet = dataSource.getConnection().getMetaData().getTables(null,schema,"%",tableTypes);
        List<String> tableNames = new ArrayList<>();
        while(resultSet.next()){
            tableNames.add(resultSet.getString("TABLE_NAME"));
        }
        log.info(":::: Tables present in DB: {}",tableNames);
    }
}
