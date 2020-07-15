package com.pernal.service;

import com.pernal.model.DataServiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public interface DataService {

    ResponseEntity<DataServiceResponse> processDataFile(MultipartFile multipartFile);

    ResponseEntity<DataServiceResponse> getData(String key);

    ResponseEntity<DataServiceResponse> deleteData(String key);
}
