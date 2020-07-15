package com.pernal.processor;

import com.pernal.exception.DataValidationException;
import com.pernal.model.Data;
import com.pernal.processor.validator.DataServiceValidator;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataFileProcessorTest {

    private DataFileProcessor dataFileProcessor = new DataFileProcessor();

    @Test
    public void shouldNotProcessForEmptyFile() {
        List<Data> data = assertDoesNotThrow(() -> dataFileProcessor.process(null));

        assertEquals(0, data.size());
    }

    @Test
    public void shouldThrowForInvalidHeader() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testDataInvalidHeader.txt");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("testDataInvalidHeader", inputStream);

        DataValidationException dataValidationException = assertThrows(DataValidationException.class, () -> dataFileProcessor.process(mockMultipartFile));
        assertEquals("Header is incorrect! The correct value is: " + DataServiceValidator.HEADER, dataValidationException.getMessage());
    }

    @Test
    public void shouldProcessForValidHeader() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testData.txt");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("testData", inputStream);

        List<Data> expectedData = createExpectedData();

        List<Data> data = assertDoesNotThrow(() -> dataFileProcessor.process(mockMultipartFile));

        assertEquals(expectedData, data);
    }

    private List<Data> createExpectedData() {
        List<Data> dataList = new ArrayList<>();

        Data data = new Data();
        data.setUpdateTimestamp(new Timestamp(15947563431L));
        data.setName("name");
        data.setDescription("desc");
        data.setPrimaryKey("String15");

        dataList.add(data);

        return dataList;
    }
}
