package com.example.taskmvc.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskModel {
    private String taskName;
    private String description;
    private String group;
    private String status;
    private long creationTime;
    private long startTime;
    private long endTime;


    public TaskModel(String taskName, String description, String group, String status, long startTime, long endTime) {
        this.taskName = taskName;
        this.description = description;
        this.group = group;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.creationTime = new Date().getTime();
    }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getGroup() { return group; }
    public void setGroup(String group) { this.group = group; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getCreationTime() { return creationTime; }
    public void setCreationTime(long creationTime) { this.creationTime = creationTime; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }

    public boolean isSameDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return sdf.format(new Date(this.startTime)).equals(sdf.format(date));
    }
}
