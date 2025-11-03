package com.example.taskmvc.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.taskmvc.Model.TaskRepository;
import com.example.taskmvc.View.Adapter.DateAdapter;
import com.example.taskmvc.View.Adapter.TaskAdapter;
import com.example.taskmvc.Controller.TaskController;
import com.example.taskmvc.Model.TaskModel;
import com.example.taskmvc.R;
import com.example.taskmvc.databinding.FragmentHomeBinding;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment implements View.OnClickListener, DateAdapter.OnDateClickListener {

    private FragmentHomeBinding binding;
    private TaskAdapter taskAdapter;
    private TaskController controller;
    private List<TaskModel> allTasks;

    private String currentStatusFilter = "All";
    private Date currentSelectedDate = new Date();
    private DateAdapter dateAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        controller = new TaskController();

        setupDateRecyclerView();
        setupTaskRecyclerView();
        setupFilterClickListeners();

        if (controller.getAllTasks().isEmpty()) createInitialData();

        loadTasks();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
    }
    private void createInitialData() {
        Calendar cal = Calendar.getInstance();
        Random random = new Random();

        String[] categories = {"Personal", "Work", "Study"};
        String[] statuses = {"To do", "In Progress", "Done"};
        String[] taskNames = {"Morning Walk","Project Meeting","Buy Groceries","Finish MAD Lab",
                "Pay Bills","Gym Session","Team Standup","Read Book",
                "Code Review","Doctor Appointment","Movie Night","Laundry"};

        for (int i = 0; i < 15; i++) {
            int dayOffset = random.nextInt(7);
            cal.set(Calendar.DAY_OF_YEAR, Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + dayOffset);
            long startTime = cal.getTimeInMillis();
            long endTime = startTime + 3600000; // 1 hour duration

            String taskName = taskNames[random.nextInt(taskNames.length)];
            String category = categories[random.nextInt(categories.length)];
            String status = statuses[random.nextInt(statuses.length)];
            String description = "Description for " + taskName;

            // The Controller creates the model and passes it to the Repository
            TaskModel newTask = new TaskModel(taskName, description, category, status, startTime, endTime);
            TaskRepository.addTask(newTask);
        }
    }

    private void loadTasks() {
        allTasks = controller.getAllTasks();
        filterTasks();
    }

    private void setupDateRecyclerView() {
        binding.rvDates.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Date> dateList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,-2);
        for (int i=0;i<=15;i++){ dateList.add(cal.getTime()); cal.add(Calendar.DAY_OF_YEAR,1); }
        dateAdapter = new DateAdapter(dateList, this);
        dateAdapter.setSelectedDate(currentSelectedDate);
        binding.rvDates.setAdapter(dateAdapter);
    }

    private void setupTaskRecyclerView() {
        binding.rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(new ArrayList<>());
        binding.rvTasks.setAdapter(taskAdapter);
    }

    private void setupFilterClickListeners() {
        binding.filterAll.setOnClickListener(this);
        binding.filterTodo.setOnClickListener(this);
        binding.filterProgress.setOnClickListener(this);
        binding.filterDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        TextView clickedFilter = (TextView) v;
        updateFilterUI(clickedFilter);
        currentStatusFilter = clickedFilter.getText().toString();
        filterTasks();
    }

    private void updateFilterUI(TextView activeFilter) {
        resetFilterStyles(binding.filterAll);
        resetFilterStyles(binding.filterTodo);
        resetFilterStyles(binding.filterProgress);
        resetFilterStyles(binding.filterDone);
        activeFilter.setBackgroundResource(R.drawable.filter_bg_active);
        activeFilter.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    private void resetFilterStyles(TextView filter) {
        filter.setBackgroundResource(R.drawable.filter_bg_inactive);
        filter.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
    }

    @Override
    public void onDateClick(Date date) {
        this.currentSelectedDate = date;
        if (dateAdapter != null) dateAdapter.setSelectedDate(currentSelectedDate);
        filterTasks();
    }

    private void filterTasks() {
        if (allTasks == null) return;
        List<TaskModel> filteredList;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        filteredList = allTasks.stream()
                .filter(task -> currentSelectedDate == null || sdf.format(new Date(task.getStartTime()))
                        .equals(sdf.format(currentSelectedDate)))
                .filter(task -> currentStatusFilter.equalsIgnoreCase("All") ||
                        (currentStatusFilter.equalsIgnoreCase("Progress") &&
                                task.getStatus().equalsIgnoreCase("In Progress")) ||
                        task.getStatus().equalsIgnoreCase(currentStatusFilter))
                .collect(Collectors.toList());

        taskAdapter.updateTasks(filteredList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
