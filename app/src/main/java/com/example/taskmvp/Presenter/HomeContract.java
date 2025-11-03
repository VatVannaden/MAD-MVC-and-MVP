package com.example.taskmvp.Presenter;

import com.example.taskmvp.Model.TaskModel;
import java.util.Date;
import java.util.List;

public interface HomeContract {

    interface View {
        void displayTasks(List<TaskModel> tasks);
        void setDateList(List<Date> dates);
        void updateFilterUI(android.view.View activeFilter);
    }

    interface Presenter {
        void attachView(View view);
        void detachView();
        void loadInitialData();
        void onDateSelected(Date date);
        void onStatusFilterChanged(String status, android.view.View activeFilter);
    }
}
