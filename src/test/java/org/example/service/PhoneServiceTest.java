package org.example.service;

import org.example.model.PhoneRecord;
import org.example.repository.PhoneRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class PhoneServiceTest {

    @MockBean
    private PhoneRepository phoneRepository;
    @MockBean
    private PhoneRecordRedisService phoneRecordRedisService;


    private List<PhoneRecord> buildTestData() {
        List<PhoneRecord> recordList = new ArrayList<>();
        PhoneRecord record = new PhoneRecord("Home", "444-444-444");
        recordList.add(record);
        return recordList;
    }


    @Test
    public void testSavePhoneRecords() {
        List<PhoneRecord> phoneRecordList = buildTestData();
        PhoneService phoneService = new PhoneService(phoneRepository, phoneRecordRedisService);

        when(phoneRecordRedisService.read(phoneRecordList.get(0))).thenReturn(null);
        phoneService.savePhoneRecords(phoneRecordList);
        verify(phoneRepository, times(phoneRecordList.size())).existsPhoneRecordByPhoneTypeAndPhoneNumber(anyString(), anyString());
        verify(phoneRepository, times(phoneRecordList.size())).save(any());
        verify(phoneRecordRedisService, times(phoneRecordList.size())).write(any());
    }

    @Test
    public void testSaveExistingPhoneRecord() {
        List<PhoneRecord> phoneRecordList = buildTestData();

        PhoneService phoneService = new PhoneService(phoneRepository, phoneRecordRedisService);
        when(phoneRecordRedisService.read(phoneRecordList.get(0))).thenReturn(phoneRecordList.get(0));
        phoneService.savePhoneRecords(phoneRecordList);
        verify(phoneRepository, times(phoneRecordList.size())).updatePhoneRecordByTypeAndNumber(anyString(), anyString());
        verify(phoneRepository, times(phoneRecordList.size())).findFirstByPhoneTypeAndPhoneNumber(anyString(), anyString());
        verify(phoneRecordRedisService, times(phoneRecordList.size())).write(any());
    }

    @Test
    public void testSaveNewPhoneRecordWhenRedisUpdateFail() {
        List<PhoneRecord> phoneRecordList = buildTestData();
        PhoneService phoneService = new PhoneService(phoneRepository, phoneRecordRedisService);
        when(phoneRecordRedisService.read(phoneRecordList.get(0))).thenReturn(null);
        when(phoneRepository.save(phoneRecordList.get(0))).thenReturn(phoneRecordList.get(0));
        doThrow(RedisConnectionFailureException.class).when(phoneRecordRedisService).write(phoneRecordList.get(0));
        try {
            phoneService.savePhoneRecords(phoneRecordList);
        }catch (Exception e){
            Assert.assertTrue(e instanceof RedisConnectionFailureException);
        }
        verify(phoneRecordRedisService, times(phoneRecordList.size() * 2)).write(any());
        verify(phoneRecordRedisService, times(phoneRecordList.size() * 2)).remove(any());
    }
}