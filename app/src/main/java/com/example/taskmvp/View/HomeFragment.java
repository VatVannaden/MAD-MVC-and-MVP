package com.example.taskmvp.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.taskmvp.R;
import com.example.taskmvp.Model.TaskModel;
import com.example.taskmvp.Presenter.HomeContract;
import com.example.taskmvp.Presenter.HomePresenter;
import com.example.taskmvp.View.Adapter.DateAdapter;
import com.example.taskmvp.View.Adapter.TaskAdapter;
import com.example.taskmvp.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View, View.OnClickListener, DateAdapter.OnDateClickListener {

    private FragmentHomeBinding binding;
    private TaskAdapter taskAdapter;
    private DateAdapter dateAdapter;
    private HomeContract.Presenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        presenter = new HomePresenter();
        presenter.attachView(this);

        setupUI();
        presenter.loadInitialData();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadInitialData();
    }

    private void setupUI() {
        binding.rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(new ArrayList<>());
        binding.rvTasks.setAdapter(taskAdapter);

        binding.rvDates.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        dateAdapter = new DateAdapter(new ArrayList<>(), this);
        binding.rvDates.setAdapter(dateAdapter);

        binding.filterAll.setOnClickListener(this);
        binding.filterTodo.setOnClickListener(this);
        binding.filterProgress.setOnClickListener(this);
        binding.filterDone.setOnClickListener(this);
    }

    @Override
    public void displayTasks(List<TaskModel> tasks) {
        taskAdapter.updateTasks(tasks);
    }

    @Override
    public void setDateList(List<Date> dates) {
        dateAdapter.updateDates(dates);
    }

    @Override
    public void updateFilterUI(View activeFilter) {
        resetFilterStyles(binding.filterAll);
        resetFilterStyles(binding.filterTodo);
        resetFilterStyles(binding.filterProgress);
        resetFilterStyles(binding.filterDone);
        activeFilter.setBackgroundResource(R.drawable.filter_bg_active);
        ((TextView) activeFilter).setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
    }

    @Override
    public void onClick(View v) {
        TextView clickedFilter = (TextView) v;
        String status = clickedFilter.getText().toString();
        presenter.onStatusFilterChanged(status, v);
    }

    @Override
    public void onDateClick(Date date) {
        dateAdapter.setSelectedDate(date);
        presenter.onDateSelected(date);
    }

    private void resetFilterStyles(TextView filter) {
        filter.setBackgroundResource(R.drawable.filter_bg_inactive);
        filter.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        binding = null;
    }
}
