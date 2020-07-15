package com.pernal.mapper;

import com.pernal.model.Data;
import com.pernal.persistence.entity.DataEntity;

public class DataMapper {

    public static Data mapToData(DataEntity dataEntity) {
        if(dataEntity != null){
            Data data = new Data();

            data.setDescription(dataEntity.getDescription());
            data.setIprimaryKey(dataEntity.getPrimaryKey());
            data.setName(dataEntity.getName());
            data.setUpdateTimestamp(dataEntity.getUpdateTimestamp());

            return data;
        }

        return null;
    }

    public static DataEntity mapToEntity(Data data){
        if(data != null){
            DataEntity dataEntity = new DataEntity();

            dataEntity.setDescription(data.getDescription());
            dataEntity.setPrimaryKey(data.getIprimaryKey());
            dataEntity.setName(data.getName());
            dataEntity.setUpdateTimestamp(data.getUpdateTimestamp());

            return dataEntity;
        }

        return null;
    }
}
