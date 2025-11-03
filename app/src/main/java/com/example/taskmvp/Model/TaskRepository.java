package com.example.taskmvp.Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class TaskRepository {
    private static TaskRepository instance;
    private final List<TaskModel> tasks = new ArrayList<>();

    public static synchronized TaskRepository getInstance() {
        if (instance == null) {
            instance = new TaskRepository();
        }
        return instance;
    }

    public List<TaskModel> getTasks() {
        return tasks;
    }

    public void addTask(TaskModel task) {
        tasks.add(task);
    }

    public void createInitialTasks() {
        if (!tasks.isEmpty()) {
            return;
        }

        Calendar cal = Calendar.getInstance();
        Random random = new Random();
        String[] categories = {"Personal", "Work", "Study"};
        String[] statuses = {"To do", "In Progress", "Done"};
        String[] taskNames = {"Morning Walk", "Project Meeting", "Buy Groceries", "Finish MAD Lab",
                "Pay Bills", "Gym Session", "Team Standup", "Read Book",
                "Code Review", "Doctor Appointment", "Movie Night", "Laundry"};

        for (int i = 0; i < 15; i++) {
            int dayOffset = random.nextInt(7);
            cal.set(Calendar.DAY_OF_YEAR, Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + dayOffset);
            long startTime = cal.getTimeInMillis();
            long endTime = startTime + 3600000;

            String taskName = taskNames[random.nextInt(taskNames.length)];
            String category = categories[random.nextInt(categories.length)];
            String status = statuses[random.nextInt(statuses.length)];
            String description = "Description for " + taskName;

            TaskModel newTask = new TaskModel(taskName, description, category, status, startTime, endTime);
            this.tasks.add(newTask);
        }
    }
}
