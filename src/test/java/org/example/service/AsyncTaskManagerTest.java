package org.example.service;

import org.example.config.async.AsyncTaskExecutor;
import org.example.config.async.AsyncTaskManager;
import org.example.config.model.TaskInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.HashMap;

public class AsyncTaskManagerTest {


    @Test
    public void testAsyncTaskManager() {
        AsyncTaskManager asyncTaskManager = new AsyncTaskManager(new HashMap<>(), new AsyncTaskExecutor());

        TaskInfo taskInfo = asyncTaskManager.submit(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Assert.assertTrue(asyncTaskManager.hasTaskRunning());
        Assert.assertTrue(StringUtils.isNotBlank(taskInfo.getTaskId()));
        TaskInfo executedTask = asyncTaskManager.getTaskInfo(taskInfo.getTaskId());
        Assert.assertNotNull(executedTask);
        Assert.assertEquals(taskInfo, executedTask);
    }

}
