package com.zsoft.domain.extension;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "persistent_configuration")
public class PersistentConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, name = "entity")
    private String entity;

    @Column(nullable = true, name = "entity_id")
    private Long entityId;

    @Column
    private String key;

    @Column
    private String value;

    public PersistentConfiguration() {
    }

    public PersistentConfiguration(String key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public PersistentConfiguration(String entity, Long entityId, String key, String value) {
        this.id = id;
        this.entity = entity;
        this.entityId = entityId;
        this.key = key;
        this.value = value;
    }

    public PersistentConfiguration(Class entity, Long entityId, String key, String value) {
        this.id = id;
        this.entity = entity.getSimpleName();
        this.entityId = entityId;
        this.key = key;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void setEntity(Class entity) {
        this.entity = entity.getSimpleName();
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PersistentConfiguration{" +
            "id=" + id +
            ", entity=" + entity +
            ", entityId='" + entityId + '\'' +
            ", key='" + key + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
