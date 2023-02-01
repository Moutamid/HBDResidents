package com.moutamid.hbdresidents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.hbdresidents.databinding.ActivityUrgentBinding;

public class UrgentActivity extends AppCompatActivity {
    ActivityUrgentBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUrgentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}