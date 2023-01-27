package com.moutamid.hbdresidents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.moutamid.hbdresidents.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MediaPlayer piano;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        piano = MediaPlayer.create(this, R.raw.piano);

        binding.signup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
            finish();
        });

        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        piano.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        piano.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        piano.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        piano.start();
    }
}