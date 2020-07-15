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
import java.util.*;

@Component
public class DataFileProcessor {

    private Logger logger = LoggerFactory.getLogger(DataFileProcessor.class);

    private DataServiceValidator dataServiceValidator;

    public DataFileProcessor(DataServiceValidator dataServiceValidator) {
        this.dataServiceValidator = dataServiceValidator;
    }

    public List<Data> process(MultipartFile multipartFile) throws DataValidationException {
        try (InputStream is = multipartFile.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String headerLine = br.readLine();

            dataServiceValidator.validateHeader(headerLine);

            return parseRows(br);
        } catch (IOException e) {
            logger.error("Error parsing file: ", e);
            return Collections.emptyList();
        }

    }

    private List<Data> parseRows(BufferedReader br) throws IOException {
        List<Data> dataList = new ArrayList<>();
        Set<String> primaryKeys = new HashSet<>();
        int lineCounter = 1;
        String line;

        while ((line = br.readLine()) != null) {
            lineCounter++;
            String[] columnsData = line.trim().split(",");

            try {
                validateRow(columnsData, primaryKeys);
            } catch (DataValidationException e) {
                logger.warn("Error in line {}, {}", lineCounter, e.getMessage());
                continue;
            }

            primaryKeys.add(columnsData[0]);
            dataList.add(parseRow(columnsData));
        }

        return dataList;
    }

    private Data parseRow(String[] columnsData) {
        Data data = new Data();

        data.setIprimaryKey(columnsData[0]);
        data.setName(columnsData[1]);
        data.setDescription(columnsData[2]);
        data.setUpdateTimestamp(new Timestamp(Long.parseLong(columnsData[3])));

        return data;
    }

    private void validateRow(String[] columnsData, Set<String> primaryKeys) throws DataValidationException {
        dataServiceValidator.validateRowDataQuantity(columnsData);
        dataServiceValidator.validatePrimaryKey(columnsData[0], primaryKeys);
        dataServiceValidator.validateTimestamp(columnsData[3]);
    }
}
