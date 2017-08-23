package com.dena.platform.restapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Javad Alimohammadi<bs.alimohammadi@yahoo.com>
 */

public class EntityDTO {

    @JsonProperty(value = "app_name", required = true)
    private String appName;

    @JsonProperty(value = "table_name", required = true)
    private String tableName;

    @JsonProperty("entity_id")
    private String entityId;

    @JsonProperty(value = "entity_type", required = true)
    private String entityType;

    @JsonProperty(value = "operation_type", required = true)
    private String operationType;

    private Map<String, Object> allPropertis = new HashMap<>();

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Map<String, Object> getAllPropertis() {
        return allPropertis;
    }

    public void setAllPropertis(Map<String, Object> allPropertis) {
        this.allPropertis = allPropertis;
    }
}
