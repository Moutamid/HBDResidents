package com.moutamid.hbdresidents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.moutamid.hbdresidents.databinding.ActivityEditProfileBinding;
import com.moutamid.hbdresidents.models.UserModel;
import com.moutamid.hbdresidents.utilis.Constants;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    UserModel userModel = dataSnapshot.getValue(UserModel.class);
                    binding.name.getEditText().setText(userModel.getName());
                    binding.residence.getEditText().setText(userModel.getResidentialArea());
                    String[] dob = userModel.getDob().split("/");
                    binding.date.getEditText().setText(dob[0]);
                    binding.month.getEditText().setText(dob[1]);
                    binding.year.getEditText().setText(dob[2]);
                    Glide.with(EditProfileActivity.this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.profileImage);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });

        binding.edit.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(624)
                    .maxResultSize(600, 600)
                    .start();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data!=null && data.getData()!= null){
                image = data.getData();
                Glide.with(EditProfileActivity.this).load(image).placeholder(R.drawable.profile_icon).into(binding.profileImage);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}