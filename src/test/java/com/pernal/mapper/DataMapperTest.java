package com.pernal.mapper;

import com.pernal.mapper.DataMapper;
import com.pernal.model.Data;
import com.pernal.persistence.entity.DataEntity;
import org.apache.tomcat.jni.Time;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;


public class DataMapperTest {

    @Test
    public void shouldMap(){
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Data data = createData(timestamp);

        DataEntity dataEntity = DataMapper.mapToEntity(data);
        Data remappedData = DataMapper.mapToData(dataEntity);

        assertEquals(data.getDescription(), remappedData.getDescription());
        assertEquals(data.getName(), remappedData.getName());
        assertEquals(data.getPrimaryKey(), remappedData.getPrimaryKey());
        assertEquals(data.getUpdateTimestamp(), remappedData.getUpdateTimestamp());
    }

    private Data createData(Timestamp timestamp) {
        Data data = new Data();

        data.setPrimaryKey("key");
        data.setDescription("desc");
        data.setName("name");
        data.setUpdateTimestamp(timestamp);

        return data;
    }

}
