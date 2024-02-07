package com.vaibhav.multi_tenant.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.boot.model.naming.Identifier;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BatchQueryGenerator<T> {
    private final String COMMA = ",";
    private final String OPEN_BRACKET = "(";
    private final String CLOSE_BRACKET = ")";
    private final String EQUALS = "=";
    private final String DOT = ".";
    private final String EXCLUDED = "EXCLUDED";

    private Identifier physicalNamingStrategy(final Identifier identifier){
        if(identifier == null){
            return identifier;
        }
        final String regex = "([a-z])([A-Z])";
        final String replacement = "$1_$2";
        final String newName = identifier.getText().replaceAll(regex,replacement).toLowerCase();
        return Identifier.toIdentifier(newName);
    }
    private List<Field> getColumnFields(Class<?> clazz){
        List<Field> fields = new ArrayList<>();
        while(clazz != Object.class){
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields.stream().filter(field -> !java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
                !List.class.isAssignableFrom(field.getType())).collect(Collectors.toList());
    }
    private String populateTableColumns(List<Field> columnFields) {
        StringBuilder queryBuilder = new StringBuilder();
        for(Field field: columnFields){
            String columnName = physicalNamingStrategy(Identifier.toIdentifier(field.getName())).getText();
            queryBuilder.append(columnName).append(COMMA);
        }
        queryBuilder.deleteCharAt(queryBuilder.lastIndexOf(COMMA));
        return queryBuilder.toString();
    }
    private String populateColumnParams(List<Field> columnFields){
        StringBuilder valuesBuilder = new StringBuilder();
        for(Field field:columnFields){
            valuesBuilder.append("?").append(COMMA);
        }
        valuesBuilder.deleteCharAt(valuesBuilder.lastIndexOf(COMMA));
        return valuesBuilder.toString();
    }
    private String populateExcludedParams(List<Field> fields){
        StringBuilder queryBuilder = new StringBuilder();
        for(Field field:fields){
            String columnName = physicalNamingStrategy(Identifier.toIdentifier(field.getName())).getText();
            queryBuilder.append(columnName).append(EQUALS).append(EXCLUDED).append(DOT).append(columnName).append(COMMA);
        }
        queryBuilder.deleteCharAt(queryBuilder.lastIndexOf(COMMA));
        return queryBuilder.toString();
    }
    private String formInitialQuery(List<Field> columnFields, String tableName) {
        return "INSERT INTO public."+
                tableName+
                OPEN_BRACKET +
                populateTableColumns(columnFields) +
                CLOSE_BRACKET+
                " VALUES "+
                OPEN_BRACKET+
                populateColumnParams(columnFields)+
                CLOSE_BRACKET+
                " ON CONFLICT(id) do UPDATE SET "+
                populateExcludedParams(columnFields);

    }
    public String upsertAll(Class<?> clazz){
        final List<Field> columnFields = getColumnFields(clazz);
        String className = clazz.getName();
        String tableName = physicalNamingStrategy(Identifier.toIdentifier(className.substring(className.lastIndexOf(DOT)+1))).getText();
        return formInitialQuery(columnFields,tableName);
    }
    public void addBatch(PreparedStatement statement, Class<?> clazz, T entity){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            List<Field> fields = getColumnFields(clazz);
            for(int i=0;i<fields.size();++i) {
                fields.get(i).setAccessible(true);
                Object fieldValue = fields.get(i).get(entity);
                if(fieldValue instanceof Long){
                    statement.setLong(i+1,(Long)fieldValue);
                }
                else
                    statement.setString(i+1,(String)fieldValue);
            }
            statement.addBatch();
        }
        catch(IllegalAccessException | SQLException ex){
            ex.printStackTrace();
        }
    }
}
