package com.example.taskmvp.Presenter;
public interface AddTaskContract {

    interface View {
        void showValidationError(String message);
        void onTaskSavedSuccess();
        void closeView();
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void saveTask(String name, String description, String group, long startTime, long endTime);
        void onCancelClicked();
    }
}