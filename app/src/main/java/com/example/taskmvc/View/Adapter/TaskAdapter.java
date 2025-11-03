package com.example.taskmvc.View.Adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmvc.Model.TaskModel;
import com.example.taskmvc.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<TaskModel> taskList;

    public TaskAdapter(List<TaskModel> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskModel task = taskList.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateTasks(List<TaskModel> newTaskList) {
        this.taskList.clear();
        this.taskList.addAll(newTaskList);
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskName;
        TextView tvTaskDescription;
        TextView tvCreationTime;
        TextView tvStatus;
        ImageView ivGroupIcon;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaskName = itemView.findViewById(R.id.tv_task_name);
            tvTaskDescription = itemView.findViewById(R.id.tv_task_description);
            tvCreationTime = itemView.findViewById(R.id.createTime);
            tvStatus = itemView.findViewById(R.id.task_status);
            ivGroupIcon = itemView.findViewById(R.id.ivGroupIcon);
        }

        public void bind(TaskModel task) {
            tvTaskName.setText(task.getTaskName());
            tvTaskDescription.setText(task.getDescription());

            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
            tvCreationTime.setText(timeFormat.format(new Date(task.getCreationTime())));

            tvStatus.setText(task.getStatus());
            updateStatusAppearance(task.getStatus(), tvStatus, itemView.getContext());

            if (ivGroupIcon != null) {
                updateGroupIcon(task.getGroup(), ivGroupIcon, itemView.getContext());
            }
        }

        private void updateStatusAppearance(String status, TextView statusView, Context context) {
            int backgroundColor;
            int textColor;

            switch (status) {
                case "In Progress":
                    backgroundColor = ContextCompat.getColor(context, R.color.status_in_progress_bg);
                    textColor = ContextCompat.getColor(context, R.color.status_in_progress_text);
                    break;
                case "Done":
                    backgroundColor = ContextCompat.getColor(context, R.color.status_done_bg);
                    textColor = ContextCompat.getColor(context, R.color.status_done_text);
                    break;
                case "To-do":
                default:
                    backgroundColor = ContextCompat.getColor(context, R.color.status_todo_bg);
                    textColor = ContextCompat.getColor(context, R.color.status_todo_text);
                    break;
            }

            if (statusView.getBackground() instanceof GradientDrawable) {
                GradientDrawable background = (GradientDrawable) statusView.getBackground();
                background.setColor(backgroundColor);
            } else {
                statusView.setBackgroundColor(backgroundColor);
            }
            statusView.setTextColor(textColor);
        }

        private void updateGroupIcon(String group, ImageView groupIconView, Context context) {
            int iconResId;

            if (group.equalsIgnoreCase("Study")) {
                iconResId = R.drawable.task_group_study;
            } else if (group.equalsIgnoreCase("Entertainment")) {
                iconResId = R.drawable.task_group_entertainment;
            } else if (group.equalsIgnoreCase("Personal")) {
                iconResId = R.drawable.task_group_personal;
            } else if (group.equalsIgnoreCase("Work")) {
                iconResId = R.drawable.task_group_work;
            } else {
                iconResId = R.drawable.task_group_work;
            }

            groupIconView.setImageResource(iconResId);
        }
    }
}