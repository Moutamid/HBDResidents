package com.moutamid.hbdresidents.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.hbdresidents.EditProfileActivity;
import com.moutamid.hbdresidents.MapsActivity;
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

        binding.logout.setOnClickListener(v-> {
            Constants.auth().signOut();
            startActivity(new Intent(context, SplashScreenActivity.class));
            getActivity().finish();
        });

        binding.delete.setOnClickListener(v -> {
            deleteUser();
        });

        binding.viewMap.setOnClickListener(v -> {
            startActivity(new Intent(context, MapsActivity.class));
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

    private void deleteUser() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.password);
        dialog.setCancelable(true);

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Deleting Your Account...");
        progressDialog.setTitle("Please Wait");

        Button close = dialog.findViewById(R.id.cancel);
        Button delete = dialog.findViewById(R.id.delete);
        EditText pass = dialog.findViewById(R.id.password);

        delete.setOnClickListener(v -> {
            if (!pass.getText().toString().isEmpty()) {
                progressDialog.show();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pass.getText().toString());
                if (user!=null) {
                    user.reauthenticate(credential).addOnSuccessListener(unused -> {
                        user.delete().addOnSuccessListener(unused1 -> {
                            progressDialog.dismiss();
                            Log.d("TAG", "User account deleted.");
                            startActivity(new Intent(context, SplashScreenActivity.class));
                            Toast.makeText(context, "Deleted User Successfully,", Toast.LENGTH_LONG).show();
                        }).addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

        close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);

    }
}