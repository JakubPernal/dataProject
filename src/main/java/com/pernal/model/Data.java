package com.pernal.model;

import java.sql.Timestamp;
import java.util.Objects;

public class Data {
    private String primaryKey;
    private String name;
    private String description;
    private Timestamp updateTimestamp;

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return Objects.equals(primaryKey, data.primaryKey) &&
                Objects.equals(name, data.name) &&
                Objects.equals(description, data.description) &&
                Objects.equals(updateTimestamp, data.updateTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryKey, name, description, updateTimestamp);
    }
}
