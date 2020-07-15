package com.pernal.controller;

import com.pernal.model.Data;
import com.pernal.model.DataServiceResponse;
import com.pernal.service.DataService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/data")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/get/{key}")
    public ResponseEntity<DataServiceResponse> getData(@PathVariable String key) {
        return dataService.getData(key);
    }

    @DeleteMapping("/delete/{key}")
    public ResponseEntity<DataServiceResponse> deleteData(@PathVariable String key) {
        return dataService.deleteData(key);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DataServiceResponse> addData(@RequestBody MultipartFile file) {
        return dataService.processDataFile(file);
    }
}
