package com.example.taskmvc.Model;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static TaskRepository instance;
    private static final List<TaskModel> tasks = new ArrayList<>();
    public static synchronized TaskRepository getInstance() {
        if (instance == null) {
            instance = new TaskRepository();
        }
        return instance;
    }

    public static void addTask(TaskModel task) {
        tasks.add(task);
    }

    public List<TaskModel> getTasks() {
        return tasks;
    }

    public void addInitialTasks() {
        if (tasks.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            tasks.add(new TaskModel("Buy groceries", "Milk, Bread, Cheese", "Personal", "To do", currentTime, currentTime + 86400000L)); // +1 day
            tasks.add(new TaskModel("Finish MAD Lab", "Complete the MVC refactoring", "Study", "In Progress", currentTime, currentTime + 172800000L)); // +2 days
            tasks.add(new TaskModel("Call the bank", "Ask about new credit card", "Personal", "Done", currentTime - 86400000L, currentTime));
        }
    }
}
