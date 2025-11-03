package com.example.taskmvp.Presenter;


import com.example.taskmvp.Model.TaskModel;
import com.example.taskmvp.Model.TaskRepository;
import java.util.Random;

public class AddTaskPresenter implements AddTaskContract.Presenter {

    private AddTaskContract.View view;
    private final TaskRepository repository;

    public AddTaskPresenter() {
        this.repository = TaskRepository.getInstance();
    }

    @Override
    public void attachView(AddTaskContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void saveTask(String name, String description, String group, long startTime, long endTime) {
        if (name.trim().isEmpty()) {
            if (view != null) view.showValidationError("Task name cannot be empty.");
            return;
        }
        if (startTime == -1) {
            if (view != null) view.showValidationError("Please select a start date.");
            return;
        }
        if (endTime == -1) {
            if (view != null) view.showValidationError("Please select an end date.");
            return;
        }
        if (endTime < startTime) {
            if (view != null) view.showValidationError("End date cannot be before start date.");
            return;
        }

        String[] statuses = {"To do", "In Progress", "Done"};
        String randomStatus = statuses[new Random().nextInt(statuses.length)];

        TaskModel newTask = new TaskModel(name, description, group, randomStatus, startTime, endTime);
        repository.addTask(newTask);

        if (view != null) {
            view.onTaskSavedSuccess();
        }
    }

    @Override
    public void onCancelClicked() {
        if (view != null) {
            view.closeView();
        }
    }
}

