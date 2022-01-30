package org.example.config.model;

import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@EqualsAndHashCode
public class TaskInfo {

    private String taskId;
    private TaskStatusEnum status;
    private Date startTime;
    private Date endTime;
    private String totalTime;


    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTotalTime() {

        if (endTime == null) {
            return "";
        } else {
            return String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(endTime.getTime() - startTime.getTime()),
                    TimeUnit.MILLISECONDS.toSeconds(endTime.getTime() - startTime.getTime()) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime.getTime() - startTime.getTime())));
        }
    }

    public void setTotalTime() {
        this.totalTime = (this.endTime.getTime() - this.startTime.getTime()) + "ms";
    }

    public enum TaskStatusEnum {
        STARTED(1, "task started"),
        RUNNING(0, "task running"),
        SUCCESS(2, "task success"),
        FAILED(-2, "task failed");
        private final int state;
        private final String stateInfo;

        TaskStatusEnum(int state, String stateInfo) {
            this.state = state;
            this.stateInfo = stateInfo;
        }

        public int getState() {
            return state;
        }

        public String getStateInfo() {
            return stateInfo;
        }
    }


}
