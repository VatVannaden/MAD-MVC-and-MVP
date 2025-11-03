package com.example.taskmvc.View.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmvc.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private final List<Date> dateList;
    private final OnDateClickListener listener;
    private int selectedPosition = -1;
    private Context context;

    public interface OnDateClickListener {
        void onDateClick(Date date);
    }

    public DateAdapter(List<Date> dateList, OnDateClickListener listener) {
        this.dateList = dateList;
        this.listener = listener;

        // Set today as initially selected
        Calendar todayCal = Calendar.getInstance();
        for (int i = 0; i < dateList.size(); i++) {
            Calendar itemCal = Calendar.getInstance();
            itemCal.setTime(dateList.get(i));
            if (todayCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
                    todayCal.get(Calendar.DAY_OF_YEAR) == itemCal.get(Calendar.DAY_OF_YEAR)) {
                this.selectedPosition = i;
                break;
            }
        }
    }

    public void setSelectedDate(Date selectedDate) {
        if (selectedDate == null) {
            // Deselect all dates
            int previousPosition = selectedPosition;
            selectedPosition = -1;
            if (previousPosition != -1) {
                notifyItemChanged(previousPosition);
            }
            return;
        }

        Calendar selectedCal = Calendar.getInstance();
        selectedCal.setTime(selectedDate);

        int newPosition = -1;
        for (int i = 0; i < dateList.size(); i++) {
            Calendar itemCal = Calendar.getInstance();
            itemCal.setTime(dateList.get(i));
            if (selectedCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR) &&
                    selectedCal.get(Calendar.DAY_OF_YEAR) == itemCal.get(Calendar.DAY_OF_YEAR)) {
                newPosition = i;
                break;
            }
        }

        if (newPosition != -1) {
            int previousPosition = selectedPosition;
            selectedPosition = newPosition;

            if (previousPosition != -1) {
                notifyItemChanged(previousPosition);
            }
            notifyItemChanged(selectedPosition);
        }
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.date, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        Date date = dateList.get(position);

        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.date_bg_active);
            holder.tvDayName.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tvDate.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tvMonthName.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tvDate.setTypeface(null, Typeface.BOLD);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.date_bg_inactive);
            holder.tvDayName.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tvDate.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tvMonthName.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tvDate.setTypeface(null, Typeface.NORMAL);
        }
        holder.bind(date, listener);
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayName, tvDate, tvMonthName;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayName = itemView.findViewById(R.id.dayName);
            tvDate = itemView.findViewById(R.id.dayNum);
            tvMonthName = itemView.findViewById(R.id.month);
        }

        public void bind(final Date date, final OnDateClickListener listener) {
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.US);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.US);
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.US);

            tvDayName.setText(dayFormat.format(date));
            tvDate.setText(dateFormat.format(date));
            tvMonthName.setText(monthFormat.format(date));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int clickedPosition = getAdapterPosition();

                    int previousPosition = selectedPosition;
                    if (clickedPosition == selectedPosition) {
                        selectedPosition = -1;
                        notifyItemChanged(previousPosition);
                        listener.onDateClick(null);
                    } else {
                        selectedPosition = clickedPosition;

                        if (previousPosition != -1) {
                            notifyItemChanged(previousPosition);
                        }
                        notifyItemChanged(selectedPosition);
                        listener.onDateClick(dateList.get(selectedPosition));
                    }
                }
            });
        }
    }
}