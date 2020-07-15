package com.pernal.model;

import java.sql.Timestamp;

public class Data {
    private String iprimaryKey;
    private String name;
    private String description;
    private Timestamp updateTimestamp;

    public String getIprimaryKey() {
        return iprimaryKey;
    }

    public void setIprimaryKey(String iprimaryKey) {
        this.iprimaryKey = iprimaryKey;
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
}
