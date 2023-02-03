package com.moutamid.hbdresidents.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.hbdresidents.ChatActivity;
import com.moutamid.hbdresidents.ComplaintActivity;
import com.moutamid.hbdresidents.MyNeighborhoodActivity;
import com.moutamid.hbdresidents.R;
import com.moutamid.hbdresidents.databinding.FragmentReachBinding;

public class ReachFragment extends Fragment {
    FragmentReachBinding binding;
    Context context;

    public ReachFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReachBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        binding.damaged.setOnClickListener(v -> {
            Intent i = new Intent(context, ComplaintActivity.class);
            i.putExtra("type", "FEED");
            startActivity(i);
        });

        binding.chat.setOnClickListener(v -> {
            String uid = Stash.getString("uid");
            if (uid.isEmpty()){
                Toast.makeText(context, "You Don't Have urgent Chat", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(context, ChatActivity.class);
                startActivity(i);
            }
        });

        binding.littering.setOnClickListener(v -> {
            /*try {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:123456789"));
                startActivity(i);
            } catch (Exception e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }*/
            Intent i = new Intent(context, ComplaintActivity.class);
            i.putExtra("type", "FEED");
            startActivity(i);
        });

        binding.complaint.setOnClickListener(v -> {
            Intent i = new Intent(context, ComplaintActivity.class);
            i.putExtra("type", "COMP");
            startActivity(i);
        });
        binding.noiseComplaint.setOnClickListener(v -> {
            Intent i = new Intent(context, ComplaintActivity.class);
            i.putExtra("type", "COMP");
            startActivity(i);
        });
        binding.feedback.setOnClickListener(v -> {
            Intent i = new Intent(context, ComplaintActivity.class);
            i.putExtra("type", "FEED");
            startActivity(i);
        });

        return view;
    }
}