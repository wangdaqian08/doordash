package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.async.AsyncTaskManager;
import org.example.config.model.TaskInfo;
import org.example.model.PhoneData;
import org.example.model.PhoneRecord;
import org.example.repository.PhoneRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@AllArgsConstructor
public class DumpService {

    private final PhoneRepository phoneRepository;
    private final AsyncTaskManager asyncTaskManager;

    /**
     * find all phone records then creat json file.
     */
    public List<TaskInfo> dumpData() {

        if (asyncTaskManager.hasTaskRunning()) {
            List<TaskInfo> runningTasks = asyncTaskManager.getRunningTasks();
            log.info("has {} running tasks", runningTasks.size());
            return runningTasks;
        } else {
            TaskInfo submit = asyncTaskManager.submit(this::findPhoneRecordsAndCreateFile);
            return List.of(submit);
        }
    }

    /**
     * no pagination  applied to this method, just proof of concept.
     */
    private void findPhoneRecordsAndCreateFile() {
        Iterable<PhoneRecord> records = phoneRepository.findAll();
        String recordString = StreamSupport.stream(records.spliterator(), true)
                .map(record -> "(" + record.getPhoneType() + ")" + record.getPhoneNumber()).collect(Collectors.joining(" "));
        log.info("data collected size:{}", records.spliterator().estimateSize());
        PhoneData phoneData = new PhoneData();
        phoneData.setRawPhoneNumbers(recordString);
        ClassPathResource testData = new ClassPathResource("test-data.json");
        writeFile(testData.getPath(), phoneData);
    }


    private void writeFile(String path, PhoneData phoneData) {

        FileWriter fileWriter;
        PrintWriter printWriter = null;
        try {
            fileWriter = new FileWriter(path);
            printWriter = new PrintWriter(fileWriter);
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
            String valueAsString = objectMapper.writeValueAsString(phoneData);
            printWriter.print(valueAsString);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
                log.info("file generated");
            }
        }
    }

    public TaskInfo findTaskById(String taskId) {
        return asyncTaskManager.getTaskInfo(taskId);
    }
}
