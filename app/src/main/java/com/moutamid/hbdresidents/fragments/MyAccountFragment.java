package com.moutamid.hbdresidents.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.hbdresidents.EditProfileActivity;
import com.moutamid.hbdresidents.R;
import com.moutamid.hbdresidents.SplashScreenActivity;
import com.moutamid.hbdresidents.databinding.FragmentMyAccountBinding;
import com.moutamid.hbdresidents.models.UserModel;
import com.moutamid.hbdresidents.utilis.Constants;

public class MyAccountFragment extends Fragment {
    FragmentMyAccountBinding binding;
    Context context;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        binding.editProfile.setOnClickListener(v -> {
            startActivity(new Intent(context, EditProfileActivity.class));
        });

        binding.logout.setOnClickListener(v->{
            Constants.auth().signOut();
            startActivity(new Intent(context, SplashScreenActivity.class));
            getActivity().finish();
        });

        binding.delete.setOnClickListener(v -> {
            Toast.makeText(context, "OK COOL", Toast.LENGTH_SHORT).show();
        });

        Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel model = snapshot.getValue(UserModel.class);
                        Glide.with(context).load(model.getImage()).placeholder(R.drawable.profile_icon).into(binding.profileImage);
                        binding.name.setText(model.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return view;
    }
}