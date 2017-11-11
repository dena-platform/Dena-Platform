package com.dena.platform.core;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class EntityDTO {
    public Map<String, Object> notMappedProperties = new HashMap<>();

    @JsonProperty(value = "app_name", required = true)
    private String appName;

    @JsonProperty(value = "table_name", required = true)
    private String tableName;

    @JsonProperty("entity_id")
    private Integer entityId;

    @JsonProperty(value = "entity_type", required = true)
    private String entityType;

    @JsonProperty(value = "operation_type", required = true)
    private String operationType;

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
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

    @JsonAnySetter
    public void addProperty(String name, Object value) {
        if (!StringUtils.isBlank(name) && value != null) {
            notMappedProperties.put(name, value);
        }

    }

    @Override
    public String toString() {
        return "EntityDTO{" +
                "notMappedProperties=" + notMappedProperties +
                ", appName='" + appName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", entityId='" + entityId + '\'' +
                ", entityType='" + entityType + '\'' +
                ", operationType='" + operationType + '\'' +
                '}';
    }
}