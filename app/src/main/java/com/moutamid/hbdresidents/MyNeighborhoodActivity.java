package com.moutamid.hbdresidents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.fxn.stash.Stash;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.hbdresidents.databinding.ActivityMyNeighborhoodBinding;
import com.moutamid.hbdresidents.fragments.HomeFragment;
import com.moutamid.hbdresidents.fragments.MyAccountFragment;
import com.moutamid.hbdresidents.fragments.ReachFragment;
import com.moutamid.hbdresidents.models.PendingModel;
import com.moutamid.hbdresidents.utilis.Constants;
import com.moutamid.hbdresidents.utilis.NotificationHelper;

public class MyNeighborhoodActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    ActivityMyNeighborhoodBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyNeighborhoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkStatus();

        binding.bottomNav.setOnNavigationItemSelectedListener(this);
        binding.bottomNav.setSelectedItemId(R.id.actionHome);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkStatus();
    }

    private void checkStatus() {
        String uid = Stash.getString("uid", "");
        Constants.databaseReference().child("pending").child(Constants.auth().getCurrentUser().getUid())
                .child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            PendingModel pending = snapshot.getValue(PendingModel.class);
                            if (pending.isPending()) {
                                NotificationHelper notificationHelper = new NotificationHelper(MyNeighborhoodActivity.this);
                                notificationHelper.sendHighPriorityNotification(
                                        "Your Wait is Over",
                                        "You can now chat with the admin",
                                        MyNeighborhoodActivity.class
                                );
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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