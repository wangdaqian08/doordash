package org.example.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;

@Entity
@Table(name = "phone_record")
@NoArgsConstructor
public class PhoneRecord{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;

    private String phoneType;

    private Integer occurrences;

    @Version
    private Long version;

    public PhoneRecord(String phoneType, String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.phoneType = phoneType;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public Integer getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(Integer occurrences) {
        this.occurrences = occurrences;
    }


}
