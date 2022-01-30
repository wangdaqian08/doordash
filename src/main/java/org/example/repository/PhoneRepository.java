package org.example.repository;

import org.example.model.PhoneRecord;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PhoneRepository extends CrudRepository<PhoneRecord, Long> {


    boolean existsPhoneRecordByPhoneTypeAndPhoneNumber(final String phoneType, final String phoneNumber);

    PhoneRecord findFirstByPhoneTypeAndPhoneNumber(final String phoneType, final String phoneNumber);

    @Transactional
    @Modifying
    @Query("update PhoneRecord p set p.occurrences=p.occurrences+1 where p.phoneType=?1 and p.phoneNumber=?2")
    void updatePhoneRecordByTypeAndNumber(final String phoneType, final String phoneNumber);
}
