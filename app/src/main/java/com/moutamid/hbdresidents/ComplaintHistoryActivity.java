package com.moutamid.hbdresidents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.moutamid.hbdresidents.databinding.ActivityComplaintHistoryBinding;

public class ComplaintHistoryActivity extends AppCompatActivity {
    ActivityComplaintHistoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}