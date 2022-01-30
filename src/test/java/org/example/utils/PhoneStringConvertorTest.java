package org.example.utils;

import org.apache.commons.lang3.StringUtils;
import org.example.model.PhoneRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PhoneStringConvertorTest {


    @Test
    public void testValidRawPhoneData() {
        String validPhoneData = "{\"(Home) 415-415-4155 (Cell) 415-123-4567\"}";
        List<PhoneRecord> records = PhoneStringConvertor.covertToPhoneData(validPhoneData);

        Assert.assertEquals(2, records.size());
        Assert.assertTrue(StringUtils.isNotBlank(records.get(0).getPhoneNumber()));
        Assert.assertTrue(StringUtils.isNotBlank(records.get(0).getPhoneType()));

        Assert.assertTrue(StringUtils.isNotBlank(records.get(1).getPhoneNumber()));
        Assert.assertTrue(StringUtils.isNotBlank(records.get(1).getPhoneType()));

    }


    @Test
    public void testPhoneRecordNoBracketsInType(){
        String validPhoneData = "{\"(Home) 415-415-4155 (Cell) 415-123-4567\"}";
        List<PhoneRecord> records = PhoneStringConvertor.covertToPhoneData(validPhoneData);

        Assert.assertFalse(StringUtils.contains(records.get(0).getPhoneType(),"("));
        Assert.assertFalse(StringUtils.contains(records.get(0).getPhoneType(),")"));
        Assert.assertFalse(StringUtils.contains(records.get(1).getPhoneType(),")"));
        Assert.assertFalse(StringUtils.contains(records.get(1).getPhoneType(),"("));
    }

    @Test
    public void testInValidRawPhoneData() {
        String validPhoneData = "{\"(Home) 415-415-4155 415-123-4567\"}";
        Assert.assertThrows(IllegalArgumentException.class, () -> PhoneStringConvertor.covertToPhoneData(validPhoneData));

        String validPhoneData2 = "{  }";
        Assert.assertThrows(IllegalArgumentException.class, () -> PhoneStringConvertor.covertToPhoneData(validPhoneData2));
    }
}