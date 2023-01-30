package com.moutamid.hbdresidents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

import com.moutamid.hbdresidents.databinding.ActivityComplaintHistoryBinding;
import com.moutamid.hbdresidents.fragments.ComplaintFragment;
import com.moutamid.hbdresidents.fragments.FeedbackFragment;

public class ComplaintHistoryActivity extends AppCompatActivity {
    ActivityComplaintHistoryBinding binding;
    ProgressDialog progressDialog;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        progressDialog.show();

        new Handler().postDelayed(() -> {progressDialog.dismiss();}, 1000);

        binding.back.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        binding.viewpager.setAdapter(viewPagerAdapter);

        binding.tablayout.setupWithViewPager(binding.viewpager);

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0)
                fragment = new ComplaintFragment();
            else if (position == 1)
                fragment = new FeedbackFragment();

            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0)
                title = "Complaint";
            else if (position == 1)
                title = "Feedback";
            return title;
        }
    }
}