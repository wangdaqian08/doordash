package org.example.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.config.model.TaskInfo;
import org.example.exception.ErrorResponse;
import org.example.model.PhoneData;
import org.example.model.PhoneRecord;
import org.example.model.Result;
import org.example.service.DumpService;
import org.example.service.PhoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.example.utils.PhoneStringConvertor.covertToPhoneData;

@AllArgsConstructor
@RestController
@RequestMapping(PhoneController.DOORDASH_BASE_URL)
@Slf4j
public class PhoneController {


    public static final String DOORDASH_BASE_URL = "/doordash";
    public static final String SAVE_PHONE_NUMBERS = "/save-phone-numbers";
    public static final String DUMP_DATA = "/dump-data";
    public static final String CHECK_TASK = "/check-task";
    private final PhoneService phoneService;
    private final DumpService dumpService;


    @PostMapping(SAVE_PHONE_NUMBERS)
    public Result savePhoneNumber(@Valid @RequestBody PhoneData phoneData) {
        if (phoneData == null || StringUtils.isBlank(phoneData.getRawPhoneNumbers())) {
            throw new IllegalArgumentException();
        }
        String rawPhoneNumbers = phoneData.getRawPhoneNumbers();
        List<PhoneRecord> phoneRecordList = covertToPhoneData(rawPhoneNumbers);
        return phoneService.savePhoneRecords(phoneRecordList);
    }


    @GetMapping(DUMP_DATA)
    public List<TaskInfo> dumpRecordsFromDatabase() {
        return dumpService.dumpData();
    }

    @GetMapping(CHECK_TASK + "/{taskId}")
    public TaskInfo dumpRecordsFromDatabase(@PathVariable("taskId") String taskId) {
        if (StringUtils.isBlank(taskId)) {
            log.error("task id is null");
            throw new IllegalArgumentException("empty task id");
        }
        return dumpService.findTaskById(taskId);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions() {
        ErrorResponse error = new ErrorResponse("invalid data input", null);
        log.error("invalid data input");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


}
