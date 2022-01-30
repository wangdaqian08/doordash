package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PhoneData {

    @JsonProperty("raw_phone_numbers")
    private String rawPhoneNumbers;
}
