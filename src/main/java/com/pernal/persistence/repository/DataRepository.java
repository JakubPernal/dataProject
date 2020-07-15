package com.pernal.persistence.repository;

import com.pernal.persistence.entity.DataEntity;

import java.math.BigDecimal;
import java.util.List;

public interface DataRepository {

    DataEntity getDataByPrimaryKey(String key);

    void deleteDataByPrimaryKey(String key);

    void createDataBatch(List<DataEntity> dataEntity);
}
