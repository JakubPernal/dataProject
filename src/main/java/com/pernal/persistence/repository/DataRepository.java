package com.pernal.persistence.repository;

import com.pernal.exception.DataPersistenceException;
import com.pernal.persistence.entity.DataEntity;

import java.util.List;

public interface DataRepository {

    DataEntity getDataByPrimaryKey(String key) throws DataPersistenceException;

    void deleteDataByPrimaryKey(String key);

    void createDataBatch(List<DataEntity> dataEntity);
}
