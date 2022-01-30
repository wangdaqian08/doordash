package org.example.service;

import org.example.config.async.AsyncTaskManager;
import org.example.config.model.TaskInfo;
import org.example.repository.PhoneRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class DumpServiceTest {

    @MockBean
    private PhoneRepository phoneRepository;
    @MockBean
    private AsyncTaskManager asyncTaskManager;


    @Test
    public void testFindTask() {
        DumpService dumpService = new DumpService(phoneRepository, asyncTaskManager);
        final String taskId = "taskId";
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskId(taskId);
        when(asyncTaskManager.getTaskInfo(taskId)).thenReturn(taskInfo);
        TaskInfo task = dumpService.findTaskById(taskId);
        Assert.assertEquals(taskId, task.getTaskId());

    }

    @Test
    public void testDumpTaskHasRunningTask() {
        DumpService dumpService = new DumpService(phoneRepository, asyncTaskManager);
        String taskId = "test";
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskId(taskId);
        taskInfo.setStatus(TaskInfo.TaskStatusEnum.RUNNING);

        List<TaskInfo> taskInfoList = List.of(taskInfo);
        when(asyncTaskManager.getRunningTasks()).thenReturn(taskInfoList);
        when(asyncTaskManager.hasTaskRunning()).thenReturn(true);
        List<TaskInfo> taskInfos = dumpService.dumpData();
        Assert.assertEquals(taskInfos.size(), taskInfoList.size());
        Assert.assertEquals(taskInfos.get(0), taskInfoList.get(0));
    }
}