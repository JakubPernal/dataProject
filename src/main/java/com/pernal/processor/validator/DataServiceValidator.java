package com.pernal.processor.validator;

import com.pernal.exception.DataValidationException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DataServiceValidator {

    public static final String HEADER = "PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP";

    public static void validateHeader(String line) throws DataValidationException {
        if (!line.trim().equals(HEADER)) {
            throw new DataValidationException("Header is incorrect! The correct value is: " + HEADER);
        }
    }

    public static void validateTimestamp(String timestamp) throws DataValidationException {
        try {
            long innerTimestamp = Long.parseLong(timestamp);
            if (new Date().compareTo(new Date(innerTimestamp)) < 0) {
                throw new DataValidationException("Invalid timestamp - future date");
            }
        } catch (NumberFormatException e) {
            throw new DataValidationException("Invalid timestamp - have to contains only numbers");
        }
    }

    public static void validateRowDataQuantity(String[] columnsData) throws DataValidationException {
        if (columnsData.length != 4) {
            throw new DataValidationException("Invalid data quantity!");
        }
    }
}
