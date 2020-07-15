package com.pernal.processor;

import com.pernal.exception.DataValidationException;
import com.pernal.model.Data;
import com.pernal.processor.validator.DataServiceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DataFileProcessor {

    private final Logger logger = LoggerFactory.getLogger(DataFileProcessor.class);

    public List<Data> process(MultipartFile multipartFile) throws DataValidationException {
        if (multipartFile != null) {
            try (InputStream is = multipartFile.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String headerLine = br.readLine();

                DataServiceValidator.validateHeader(headerLine);

                return processRows(br);
            } catch (IOException e) {
                logger.error("Error parsing file: ", e);
                return Collections.emptyList();
            }
        } else {
            logger.warn("File was null!");
            return Collections.emptyList();
        }
    }

    private List<Data> processRows(BufferedReader br) throws IOException {
        List<Data> dataList = new ArrayList<>();
        int lineCounter = 1;
        String line;

        while ((line = br.readLine()) != null) {
            lineCounter++;
            String[] columnsData = line.trim().split(",");

            try {
                validateRow(columnsData);
            } catch (DataValidationException e) {
                logger.warn("Error in line {}, {}", lineCounter, e.getMessage());
                continue;
            }

            dataList.add(parseRow(columnsData));
        }

        return dataList;
    }

    public Data parseRow(String[] columnsData) {
        Data data = new Data();

        data.setPrimaryKey(columnsData[0]);
        data.setName(columnsData[1]);
        data.setDescription(columnsData[2]);
        data.setUpdateTimestamp(new Timestamp(Long.parseLong(columnsData[3])));

        return data;
    }

    private void validateRow(String[] columnsData) throws DataValidationException {
        DataServiceValidator.validateRowDataQuantity(columnsData);
        DataServiceValidator.validateTimestamp(columnsData[3]);
    }
}
