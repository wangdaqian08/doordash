package org.example.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.annotation.RetryProcess;
import org.example.model.PhoneRecord;
import org.example.model.Result;
import org.example.repository.PhoneRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PhoneService {


    private final PhoneRepository phoneRepository;

    private final PhoneRecordRedisService phoneRecordRedisService;


    public Result savePhoneRecords(List<PhoneRecord> recordList) {
        List<PhoneRecord> savedPhoneRecords = processPhoneRecords(recordList);
        return new Result(savedPhoneRecords);
    }

    private List<PhoneRecord> processPhoneRecords(List<PhoneRecord> recordList) {
        return recordList.stream()
                .map(this::getPhoneRecord).collect(Collectors.toList());
    }

    @RetryProcess
    private PhoneRecord getPhoneRecord(PhoneRecord record) {
        PhoneRecord storedPhoneRecord;
        storedPhoneRecord = insertOrUpdatePhoneRecordWithRedis(record);
        return storedPhoneRecord;
    }

    public PhoneRecord insertOrUpdatePhoneRecord(PhoneRecord phoneRecord) {
        if (phoneRepository.existsPhoneRecordByPhoneTypeAndPhoneNumber(phoneRecord.getPhoneType(), phoneRecord.getPhoneNumber())) {
            return updatePhoneRecord(phoneRecord);
        } else {
            PhoneRecord record;
            if (phoneRepository.existsPhoneRecordByPhoneTypeAndPhoneNumber(phoneRecord.getPhoneType(), phoneRecord.getPhoneNumber())) {
                record = updatePhoneRecord(phoneRecord);
            } else {
                record = insertNewPhoneRecord(phoneRecord);
            }
            return record;
        }
    }

    private PhoneRecord insertOrUpdatePhoneRecordWithRedis(PhoneRecord phoneRecord) {
        PhoneRecord readFromRedis = phoneRecordRedisService.read(phoneRecord);
        if (readFromRedis != null) {
            return updatePhoneRecord(readFromRedis);
        } else {
            PhoneRecord record;
            if (phoneRepository.existsPhoneRecordByPhoneTypeAndPhoneNumber(phoneRecord.getPhoneType(), phoneRecord.getPhoneNumber())) {
                record = updatePhoneRecord(phoneRecord);
            } else {
                record = insertNewPhoneRecord(phoneRecord);
            }
            return record;
        }
    }

    /**
     * try to insert a new record, but when currency or duplicated records,
     * will update the occurrences.
     *
     * @param phoneRecord record to save
     * @return save record
     */
    private PhoneRecord insertNewPhoneRecord(PhoneRecord phoneRecord) {
        try {
            phoneRecord.setOccurrences(1);
            PhoneRecord saveRecord = phoneRepository.save(phoneRecord);
            updateToRedis(saveRecord);
            return saveRecord;
        } catch (DataIntegrityViolationException e) {
            log.warn("error:{}, save phone with same type and number", e.getMessage());
            return updatePhoneRecord(phoneRecord);
        }
    }


    /**
     * try to update the redis cache, if fail will retry 1 more time.
     * @param saveRecord records need to be updated in redis
     */
    private void updateToRedis(PhoneRecord saveRecord) {
        try{
            phoneRecordRedisService.remove(saveRecord);
            phoneRecordRedisService.write(saveRecord);
        }catch (Exception e){
            log.error("retry redis update");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                log.error(ex.getMessage());
                throw new RuntimeException(ex);
            }
            phoneRecordRedisService.remove(saveRecord);
            phoneRecordRedisService.write(saveRecord);
        }
    }

    private PhoneRecord updatePhoneRecord(PhoneRecord phoneRecord) {
        phoneRepository.updatePhoneRecordByTypeAndNumber(phoneRecord.getPhoneType(), phoneRecord.getPhoneNumber());
        PhoneRecord record = phoneRepository.findFirstByPhoneTypeAndPhoneNumber(phoneRecord.getPhoneType(), phoneRecord.getPhoneNumber());
        updateToRedis(record);
        return record;
    }

}
