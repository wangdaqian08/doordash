package org.example.config.async;

import lombok.AllArgsConstructor;
import org.example.config.model.TaskInfo;
import org.example.exception.AsyncTaskConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AsyncTaskManager {

    private Map<String, TaskInfo> taskContainer = new ConcurrentHashMap<>(16);

    private final AsyncTaskExecutor asyncTaskExecutor;

    public TaskInfo initTask() {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskId(getTaskId());
        taskInfo.setStatus(TaskInfo.TaskStatusEnum.STARTED);
        taskInfo.setStartTime(new Date());
        setTaskInfo(taskInfo);
        return taskInfo;
    }


    public TaskInfo submit(AsyncTaskConstructor asyncTaskConstructor) {
        TaskInfo info = initTask();
        String taskId = info.getTaskId();
        asyncTaskExecutor.executor(asyncTaskConstructor, taskId);
        return info;
    }


    public void setTaskInfo(TaskInfo taskInfo) {
        taskContainer.put(taskInfo.getTaskId(), taskInfo);
    }


    public TaskInfo getTaskInfo(String taskId) {
        return taskContainer.get(taskId);
    }

    public boolean hasTaskRunning() {
        return taskContainer.entrySet().stream()
                .anyMatch(entry -> {
                    return entry.getValue().getStatus().equals(TaskInfo.TaskStatusEnum.STARTED) ||
                            entry.getValue().getStatus().equals(TaskInfo.TaskStatusEnum.RUNNING);
                });
    }


    public TaskInfo.TaskStatusEnum getTaskStatus(String taskId) {
        return getTaskInfo(taskId).getStatus();
    }


    public String getTaskId() {
        return UUID.randomUUID().toString();
    }

    public List<TaskInfo> getRunningTasks() {
        return taskContainer.values().stream()
                .filter(taskInfo -> taskInfo.getStatus().equals(TaskInfo.TaskStatusEnum.RUNNING)
                        || taskInfo.getStatus().equals(TaskInfo.TaskStatusEnum.STARTED))
                .collect(Collectors.toList());
    }
}
