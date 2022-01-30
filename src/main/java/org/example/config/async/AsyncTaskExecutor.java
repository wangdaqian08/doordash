package org.example.config.async;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.AsyncTaskConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncTaskExecutor {
    @Async
    public void executor(AsyncTaskConstructor asyncTaskConstructor, String taskInfo) {
        log.info("AsyncTaskExecutor is executing async task:{}", taskInfo);
        asyncTaskConstructor.async();
    }
}