package com.vaibhav.multi_tenant.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaibhav.multi_tenant.model.tenant.User;
import com.vaibhav.multi_tenant.tenant.MultiTenantDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class PpoService {
    private final Map<String, List<User>> mp = new HashMap<>();
    @Value("${dbOperator.batchSize}")
    private int batchSize;

    @Autowired
    private MultiTenantDataSource dataSource;

    @Autowired
    BatchQueryGenerator<User> userBatchQueryGenerator;
    public void saveData(String tenantId, String data) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(data,User.class);
        mp.computeIfAbsent(tenantId,(k)->new ArrayList<>()).add(user);
        if(mp.get(tenantId).size()>=batchSize){
            saveBatch(tenantId,mp.get(tenantId));
        }
    }
    public void saveBatch(String tenantId, List<User> userList){
        String insertUser = userBatchQueryGenerator.upsertAll(User.class);
        log.debug(insertUser);
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement(insertUser);
            for(User user:userList){
                userBatchQueryGenerator.addBatch(statement,User.class,user);
            }
            int[] result = statement.executeBatch();
            log.info("Batch executed. Number of User rows affected: {} in {}",result.length,tenantId);
        }
        catch(SQLException ex){
            log.error("Error connecting db: {}", tenantId);
            ex.printStackTrace();
        }
    }
}
