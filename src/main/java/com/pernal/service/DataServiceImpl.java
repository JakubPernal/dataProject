package com.pernal.service;

import com.pernal.exception.DataPersistenceException;
import com.pernal.exception.DataValidationException;
import com.pernal.mapper.DataMapper;
import com.pernal.model.Data;
import com.pernal.model.DataServiceResponse;
import com.pernal.persistence.entity.DataEntity;
import com.pernal.persistence.repository.DataRepository;
import com.pernal.processor.DataFileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataServiceImpl implements DataService {

    private Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

    private DataRepository dataRepository;
    private DataFileProcessor dataFileProcessor;

    public DataServiceImpl(DataRepository dataRepository, DataFileProcessor dataFileProcessor) {
        this.dataRepository = dataRepository;
        this.dataFileProcessor = dataFileProcessor;
    }

    @Override
    public ResponseEntity<DataServiceResponse> processDataFile(MultipartFile multipartFile) {
        try {
            List<Data> dataList = dataFileProcessor.process(multipartFile);
            List<DataEntity> dataEntities = dataList.stream().map(DataMapper::mapToEntity).collect(Collectors.toList());

            dataRepository.createDataBatch(dataEntities);

            return ResponseEntity.ok(DataServiceResponse.emptyBodyResponse(HttpStatus.OK, "Data has been uploaded - for possible errors check logs"));
        } catch (DataValidationException e) {
            logger.error("Validation error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DataServiceResponse.emptyBodyResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<DataServiceResponse> getData(String key) {
        try {
            DataEntity dataEntity = dataRepository.getDataByPrimaryKey(key);
            Data data = DataMapper.mapToData(dataEntity);

            return ResponseEntity.ok(DataServiceResponse.createResponse(data, HttpStatus.OK, "OK"));
        } catch (DataPersistenceException e) {
            logger.warn(e.getMessage());
            return ResponseEntity.ok(DataServiceResponse.createResponse(null, HttpStatus.NOT_FOUND, "Entity with given id not found in database"));
        }
    }

    @Override
    public ResponseEntity<DataServiceResponse> deleteData(String key) {
        dataRepository.deleteDataByPrimaryKey(key);

        return ResponseEntity.ok(DataServiceResponse.emptyBodyResponse(HttpStatus.OK, "OK"));
    }
}
