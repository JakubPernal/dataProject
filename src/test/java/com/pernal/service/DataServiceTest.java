package com.pernal.service;

import com.pernal.exception.DataValidationException;
import com.pernal.model.DataServiceResponse;
import com.pernal.persistence.repository.DataRepositoryImpl;
import com.pernal.processor.DataFileProcessor;
import com.pernal.service.DataServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DataServiceTest {

    @Mock
    private DataRepositoryImpl dataRepository;

    @Mock
    private DataFileProcessor dataFileProcessor;

    @InjectMocks
    private DataServiceImpl dataService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldProcessDataFile() throws DataValidationException {
        MockMultipartFile file = new MockMultipartFile("name", new byte[0]);

        given(dataFileProcessor.process(file)).willReturn(Collections.emptyList());

        ResponseEntity<DataServiceResponse> processDataFileResponse = dataService.processDataFile(file);

        verify(dataFileProcessor, times(1)).process(file);
        verify(dataRepository, times(1)).createDataBatch(any());
        assertEquals(200, processDataFileResponse.getBody().getStatus().value());
        assertEquals("Data has been uploaded - for possible errors check logs", processDataFileResponse.getBody().getMessage());
    }

    @Test
    public void shouldProcessDataFileWithException() throws DataValidationException {
        MockMultipartFile file = new MockMultipartFile("name", new byte[0]);
        String errorMsg = "Error msg";

        given(dataFileProcessor.process(file)).willThrow(new DataValidationException(errorMsg));

        ResponseEntity<DataServiceResponse> processDataFileResponse = dataService.processDataFile(file);

        verify(dataFileProcessor, times(1)).process(file);
        verify(dataRepository, times(0)).createDataBatch(any());
        assertEquals(500, processDataFileResponse.getBody().getStatus().value());
        assertEquals(errorMsg, processDataFileResponse.getBody().getMessage());
    }
}
