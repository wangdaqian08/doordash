package org.example.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.model.PhoneRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@AllArgsConstructor
public class PhoneRecordRedisService {



    private final RedisTemplate<String, PhoneRecord> redisTemplate;

    /**
     * save record to redis,with random
     * @param phoneRecord record to save
     */
    public void write(PhoneRecord phoneRecord) {
        String redisKey = redisKey(phoneRecord);
        redisTemplate.opsForValue().set(redisKey, phoneRecord, getRange(1,5), TimeUnit.MINUTES);
    }

    public PhoneRecord read(PhoneRecord phoneRecord) {
        String redisKey = redisKey(phoneRecord);
        return redisTemplate.opsForValue().get(redisKey);
    }

    public void remove(PhoneRecord phoneRecord){
        redisTemplate.delete(redisKey(phoneRecord));
    }

    public static String redisKey(PhoneRecord phoneRecord) {
        if (phoneRecord == null || StringUtils.isBlank(phoneRecord.getPhoneType()) || StringUtils.isBlank(phoneRecord.getPhoneNumber())) {
            log.error("invalid phone records object");
            throw new IllegalArgumentException("phone records is null or empty string for type or number");
        }
        return phoneRecord.getPhoneType() + "-" + phoneRecord.getPhoneNumber();
    }

    public int getRange(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }
}
