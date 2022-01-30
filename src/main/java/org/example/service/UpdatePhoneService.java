package org.example.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.PhoneRecord;
import org.example.repository.PhoneRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UpdatePhoneService {
    private final PhoneRepository phoneRepository;


    //    @Transactional(propagation= Propagation.REQUIRED)
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public PhoneRecord updatePhoneRecord(final PhoneRecord phoneRecord) {
        try {

            PhoneRecord save = phoneRepository.save(phoneRecord);
            return save;
        } catch (Exception e) {
            throw new RuntimeException("runtime");
        }

//        try {
//
//        } catch (StaleObjectStateException | ObjectOptimisticLockingFailureException exception) {
//            log.error("error again");
//            Optional<PhoneRecord> updatedRecord = phoneRepository.findById(phoneRecord.getId());
////            PhoneRecord updatedRecord = phoneRepository.findFirstByPhoneTypeAndPhoneNumber(phoneRecord.getPhoneType(), phoneRecord.getPhoneNumber());
//            updatedRecord.get().setOccurrences(updatedRecord.get().getOccurrences() + 1);
//            return updatePhoneRecord(updatedRecord.get());
//        }
    }
}
