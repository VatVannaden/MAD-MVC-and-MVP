package com.example.taskmvp.Presenter;

import com.example.taskmvp.Model.TaskModel;
import com.example.taskmvp.Model.TaskRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class HomePresenter implements HomeContract.Presenter {

    private HomeContract.View view;
    private final TaskRepository repository;
    private List<TaskModel> allTasks;
    private Date currentSelectedDate = new Date();
    private String currentStatusFilter = "All";

    public HomePresenter() {
        this.repository = TaskRepository.getInstance();
    }

    @Override
    public void attachView(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadInitialData() {
        repository.createInitialTasks();
        allTasks = repository.getTasks();

        List<Date> dateList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -2);
        for (int i = 0; i <= 15; i++) {
            dateList.add(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }
        if (view != null) {
            view.setDateList(dateList);
            filterAndDisplayTasks();
        }
    }

    @Override
    public void onDateSelected(Date date) {
        this.currentSelectedDate = date;
        if (date == null) {
            if (view != null) {
                List<TaskModel> allTasks = repository.getTasks();
                view.displayTasks(allTasks);
            }
            return;
            }
        filterAndDisplayTasks();
    }

    @Override
    public void onStatusFilterChanged(String status, android.view.View activeFilter) {
        this.currentStatusFilter = status;
        if (view != null) {
            view.updateFilterUI(activeFilter);
            filterAndDisplayTasks();
        }
    }

    private void filterAndDisplayTasks() {
        if (allTasks == null) return;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        List<TaskModel> filteredList = allTasks.stream()
                .filter(task -> sdf.format(new Date(task.getStartTime())).equals(sdf.format(currentSelectedDate)))
                .filter(task -> currentStatusFilter.equalsIgnoreCase("All") ||
                        (currentStatusFilter.equalsIgnoreCase("Progress") && task.getStatus().equalsIgnoreCase("In Progress")) ||
                        task.getStatus().equalsIgnoreCase(currentStatusFilter))
                .collect(Collectors.toList());

        if (view != null) {
            view.displayTasks(filteredList);
        }
    }
}