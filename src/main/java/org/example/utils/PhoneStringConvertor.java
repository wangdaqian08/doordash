package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.model.PhoneRecord;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PhoneStringConvertor {

    /**
     * convert string to PhoneRecord object
     * for example: (Home) 415-415-4155 (Cell) 415-123-4567
     *
     * @param rawPhoneNumbers raw data
     * @return list of object
     */
    public static List<PhoneRecord> covertToPhoneData(final String rawPhoneNumbers) {

        validateRawData(rawPhoneNumbers);

        String[] rawPhoneData = rawPhoneNumbers.split(" ");

        List<PhoneRecord> records = new ArrayList<>();
        for (int i = 0; i <= rawPhoneData.length - 2; i += 2) {
            String type = rawPhoneData[i].replaceAll("\\(", "").replaceAll("\\)", "");
            String phoneNumber = rawPhoneData[i + 1];
            records.add(new PhoneRecord(type, phoneNumber));
        }
        return records;
    }

    private static void validateRawData(String rawPhoneNumbers) {
        if (!StringUtils.isNotBlank(rawPhoneNumbers)) {
            log.error("raw phone number is empty string:{}", rawPhoneNumbers);
            throw new IllegalArgumentException("invalid input, empty string");
        }

        String[] rawPhoneData = rawPhoneNumbers.split(" ");
        if (rawPhoneData.length % 2 != 0) {
            log.error("invalid raw phone data:{}, phone array length:{}", rawPhoneNumbers, rawPhoneData.length);
            throw new IllegalArgumentException("invalid input, phone array length is odd");
        }
    }
}
