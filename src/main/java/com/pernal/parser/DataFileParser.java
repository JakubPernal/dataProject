package com.pernal.parser;

import com.pernal.exception.DataValidationException;
import com.pernal.model.Data;
import com.pernal.parser.validator.DataServiceValidator;
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
public class DataFileParser {

    private Logger logger = LoggerFactory.getLogger(DataFileParser.class);

    private DataServiceValidator dataServiceValidator;

    public DataFileParser(DataServiceValidator dataServiceValidator) {
        this.dataServiceValidator = dataServiceValidator;
    }

    public List<Data> parseData(MultipartFile multipartFile) throws DataValidationException {
        try (InputStream is = multipartFile.getInputStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            List<Data> dataList = new ArrayList<>();
            Set<String> primaryKeys = new HashSet<>();
            String line = br.readLine();
            int lineCounter = 1;

            dataServiceValidator.validateHeader(line);

            while ((line = br.readLine()) != null) {
                lineCounter++;
                String[] columnsData = line.trim().split(",");

                if (columnsData.length == 4) {
                    String primaryKey = columnsData[0];
                    String timestamp = columnsData[3];

                    if (isInvalidLineData(lineCounter, primaryKeys, primaryKey, timestamp)){
                        continue;
                    }

                    Data data = new Data();

                    data.setIprimaryKey(primaryKey);
                    data.setName(columnsData[1]);
                    data.setDescription(columnsData[2]);
                    data.setUpdateTimestamp(new Timestamp(Long.parseLong(timestamp)));

                    primaryKeys.add(primaryKey);
                    dataList.add(data);
                } else {
                    logger.warn("Error while parsing line: {}, invalid data", lineCounter);
                }
            }

            return dataList;
        } catch (IOException e) {
            logger.error("Error parsing file: ", e);
            return Collections.emptyList();
        }

    }

    private boolean isInvalidLineData(int lineCounter, Set<String> primaryKeys, String primaryKey, String timestamp) {
        try{
            dataServiceValidator.validatePrimaryKey(primaryKey, primaryKeys);
            dataServiceValidator.validateTimestamp(timestamp);
        } catch (DataValidationException e){
            logger.warn("Error in line {}, {}", lineCounter, e.getMessage());
            return true;
        }

        return false;
    }
}
