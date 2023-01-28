package com.moutamid.hbdresidents;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.moutamid.hbdresidents.databinding.ActivityEditProfileBinding;
import com.moutamid.hbdresidents.models.UserModel;
import com.moutamid.hbdresidents.utilis.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    Uri image;
    Date d;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating Your Profile...");
        progressDialog.setCancelable(false);

        d = new Date();

        binding.back.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

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

        binding.update.setOnClickListener(v -> {
            progressDialog.show();
            if (image!=null) {
                uploadImage();
            } else {
                uploadData("");
            }
        });

    }

    private void uploadData(String image) {
        Map<String, Object> map = new HashMap<>();
        String dob = binding.date.getEditText().getText().toString() + "/" + binding.month.getEditText().getText().toString() + "/" + binding.year.getEditText().getText().toString();
        map.put("name", binding.name.getEditText().getText().toString());
        map.put("residentialArea", binding.residence.getEditText().getText().toString());
        map.put("dob", dob);
        map.put("image", image);

        Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                .updateChildren(map).addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    onBackPressed();
                    Toast.makeText(getApplicationContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadImage() {
        Constants.storageReference(Constants.auth().getCurrentUser().getUid())
                .child("logo").child(Constants.auth().getCurrentUser().getUid() + d.getTime())
                .putFile(image).addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadData(uri.toString());
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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