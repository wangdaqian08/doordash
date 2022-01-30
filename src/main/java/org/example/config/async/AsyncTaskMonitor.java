package org.example.config.async;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.config.model.TaskInfo;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Aspect
@AllArgsConstructor
@Slf4j
public class AsyncTaskMonitor {

    private final AsyncTaskManager manager;

    @Around("execution(* org.example.config.async.AsyncTaskExecutor.*(..))")
    public void taskHandle(ProceedingJoinPoint pjp) {

        String taskId = pjp.getArgs()[1].toString();
        TaskInfo taskInfo = manager.getTaskInfo(taskId);
        log.info("AsyncTaskMonitor is monitoring async task:{}", taskId);
        taskInfo.setStatus(TaskInfo.TaskStatusEnum.RUNNING);
        manager.setTaskInfo(taskInfo);
        TaskInfo.TaskStatusEnum status = null;
        try {
            pjp.proceed();
            status = TaskInfo.TaskStatusEnum.SUCCESS;
        } catch (Throwable throwable) {
            status = TaskInfo.TaskStatusEnum.FAILED;
            log.error("AsyncTaskMonitor:async task {} is failed.Error info:{}", taskId, throwable.getMessage());
        }
        taskInfo.setEndTime(new Date());
        taskInfo.setStatus(status);
        taskInfo.setTotalTime();
        manager.setTaskInfo(taskInfo);
    }
}
