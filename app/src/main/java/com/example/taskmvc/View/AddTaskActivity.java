package com.example.taskmvc.View;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmvc.R;
import com.example.taskmvc.View.AddTaskFragment;

public class AddTaskActivity extends AppCompatActivity implements AddTaskFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_add_task);
    }

    @Override
    public void onTaskSaved() {
        finish();
    }

    @Override
    public void onCancelPressed() {
        finish();
    }
}
