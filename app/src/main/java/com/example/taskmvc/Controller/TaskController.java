package com.example.taskmvc.Controller;

import android.content.Context;
import android.widget.Toast;

import com.example.taskmvc.Model.TaskModel;
import com.example.taskmvc.Model.TaskRepository;
import java.util.List;
import java.util.Random;

public class TaskController {

    private TaskRepository taskRepository;

    public TaskController() {
        this.taskRepository = TaskRepository.getInstance();
    }

    public boolean saveTask(Context context, String taskName, String description, String group, long startTime, long endTime) {
        if (taskName.isEmpty()) {
            showToast(context, "Task name cannot be empty.");
            return false;
        }
        if (startTime == -1) {
            showToast(context, "Please select a start date.");
            return false;
        }
        if (endTime == -1) {
            showToast(context, "Please select an end date.");
            return false;
        }
        if (endTime < startTime) {
            showToast(context, "End date cannot be before the start date.");
            return false;
        }

        String[] statuses = {"To do", "In Progress", "Done"};
        String randomStatus = statuses[new Random().nextInt(statuses.length)];

        try {
            TaskModel newTask = new TaskModel(taskName, description, group, randomStatus, startTime, endTime);
            taskRepository.addTask(newTask);
            showToast(context, "Task added successfully!");
            return true;
        } catch (Exception e) {
            showToast(context, "Failed to add task: " + e.getMessage());
            return false;
        }
    }

    public List<TaskModel> getAllTasks() {
        return taskRepository.getTasks();
    }

    public void createInitialTasks() {
        taskRepository.addInitialTasks();
    }

    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
