package com.moutamid.hbdresidents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.moutamid.hbdresidents.databinding.ActivityComplaintBinding;

public class ComplaintActivity extends AppCompatActivity {
    ActivityComplaintBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}