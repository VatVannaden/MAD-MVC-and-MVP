package com.example.taskmvp.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taskmvp.R;
import com.example.taskmvp.View.HomeFragment;
import com.example.taskmvp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private View lastSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setItemIconTintList(null);
        binding.bottomNavigation.post(() -> {
            int initialItemId = binding.bottomNavigation.getSelectedItemId();
            lastSelectedItem = binding.bottomNavigation.findViewById(initialItemId);
            if (lastSelectedItem != null) {
                lastSelectedItem.setBackground(ContextCompat.getDrawable(this, R.drawable.icon_active_bg));
            }
        });



        binding.addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });


        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            View selectedItemView = binding.bottomNavigation.findViewById(item.getItemId());
            if (lastSelectedItem != null) {
                lastSelectedItem.setBackground(null);
            }
            if (selectedItemView != null) {
                selectedItemView.setBackground(ContextCompat.getDrawable(this, R.drawable.icon_active_bg));
            }
            lastSelectedItem = selectedItemView;

            return true;
        });

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.mainFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
