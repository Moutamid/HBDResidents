package com.moutamid.hbdresidents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moutamid.hbdresidents.databinding.ActivityMyNeighborhoodBinding;
import com.moutamid.hbdresidents.fragments.HomeFragment;
import com.moutamid.hbdresidents.fragments.MyAccountFragment;
import com.moutamid.hbdresidents.fragments.ReachFragment;

public class MyNeighborhoodActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ActivityMyNeighborhoodBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyNeighborhoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNav.setOnNavigationItemSelectedListener(this);
        binding.bottomNav.setSelectedItemId(R.id.actionHome);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionHome:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout , new HomeFragment()).commit();
                return true;

            case R.id.actionAccount:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new MyAccountFragment()).commit();
                return true;

            case R.id.actionReach:
                getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new ReachFragment()).commit();
                return true;

        }
        return false;
    }
}