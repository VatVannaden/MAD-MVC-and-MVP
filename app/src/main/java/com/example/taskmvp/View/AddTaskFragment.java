package com.example.taskmvp.View;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.taskmvp.databinding.FragmentAddTaskBinding;
import com.example.taskmvp.Presenter.AddTaskContract;
import com.example.taskmvp.Presenter.AddTaskPresenter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskFragment extends Fragment implements AddTaskContract.View {

    private FragmentAddTaskBinding binding;
    private long startTimeMillis = -1;
    private long endTimeMillis = -1;
    private AddTaskContract.Presenter presenter;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onTaskSaved();
        void onCancelPressed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false);
        presenter = new AddTaskPresenter();
        presenter.attachView(this);

        setupGroupSpinner();
        setupDatePickers();

        binding.createTaskBtn.setOnClickListener(v -> handleSaveTask());
        binding.imageView3.setOnClickListener(v -> presenter.onCancelClicked());

        return binding.getRoot();
    }

    private void handleSaveTask() {
        String title = binding.etTaskName.getText().toString().trim();
        String description = binding.etTaskDescription.getText().toString().trim();
        String group = binding.spinnerGroup.getSelectedItem().toString();

        presenter.saveTask(title, description, group, startTimeMillis, endTimeMillis);
    }


    private void setupGroupSpinner() {
        String[] groups = {"Work", "Study", "Entertainment", "Personal"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, groups);
        binding.spinnerGroup.setAdapter(adapter);
    }

    private void setupDatePickers() {
        binding.btnStartDate.setOnClickListener(v -> showDatePickerDialog(binding.startDate, true));
        binding.btnEndDate.setOnClickListener(v -> showDatePickerDialog(binding.endDate, false));
    }

    private void showDatePickerDialog(TextView dateTextView, boolean isStart) {
        Calendar calendar = Calendar.getInstance();
        long initialDate = isStart ? startTimeMillis : endTimeMillis;
        if (initialDate != -1) {
            calendar.setTimeInMillis(initialDate);
        }

        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.US);
            dateTextView.setText(sdf.format(calendar.getTime()));
            if (isStart) {
                startTimeMillis = calendar.getTimeInMillis();
            } else {
                endTimeMillis = calendar.getTimeInMillis();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showValidationError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskSavedSuccess() {
        Toast.makeText(getContext(), "Task added successfully!", Toast.LENGTH_SHORT).show();
        // Use listener to tell the Activity to close this fragment
        if (mListener != null) {
            mListener.onTaskSaved();
        }
    }

    @Override
    public void closeView() {
        if (mListener != null) {
            mListener.onCancelPressed();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        binding = null;
    }
}
