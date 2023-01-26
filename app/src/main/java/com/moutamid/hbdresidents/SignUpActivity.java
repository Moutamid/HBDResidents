package com.moutamid.hbdresidents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.moutamid.hbdresidents.databinding.ActivitySignUpBinding;
import com.moutamid.hbdresidents.models.UserModel;
import com.moutamid.hbdresidents.utilis.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    ProgressDialog progressDialog;

    Uri image;
    Date d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Your Account...");
        progressDialog.setCancelable(false);

        d = new Date();

        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        binding.profileImage.setOnClickListener(v -> {
            ImagePicker.with(this)
                    .cropSquare()
                    .compress(624)
                    .maxResultSize(600, 600)
                    .start();
        });

        binding.signup.setOnClickListener(v -> {
            if (valid()){
                progressDialog.show();
                Constants.auth().createUserWithEmailAndPassword(
                        binding.email.getEditText().getText().toString(),
                        binding.password.getEditText().getText().toString()
                ).addOnSuccessListener(authResult -> {
                    if (image!=null) {
                        Constants.storageReference(authResult.getUser().getUid())
                                .child("logo").child(authResult.getUser().getUid() + d.getTime())
                                .putFile(image).addOnSuccessListener(taskSnapshot -> {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                                        uploadData(authResult.getUser().getUid(), uri.toString());
                                    }).addOnFailureListener(e -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                                }).addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        uploadData(authResult.getUser().getUid(), "");
                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void uploadData(String authResult, String uri) {
        String dob = binding.date.getEditText().getText().toString() + "/" + binding.month.getEditText().getText().toString() + "/" + binding.year.getEditText().getText().toString();
        UserModel model = new UserModel(
                authResult,
                binding.name.getEditText().getText().toString(),
                binding.residence.getEditText().getText().toString(),
                dob,
                binding.email.getEditText().getText().toString(),
                binding.password.getEditText().getText().toString(),
                uri
        );
        Constants.databaseReference().child("users").child(authResult)
                .setValue(model).addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    startActivity(new Intent(SignUpActivity.this, MyNeighborhoodActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean valid() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String d = format.format(date);
        Toast.makeText(this, d, Toast.LENGTH_SHORT).show();
        if (binding.name.getEditText().getText().toString().isEmpty()) {
            binding.name.getEditText().setError("Name is Required");
            return false;
        } if (binding.residence.getEditText().getText().toString().isEmpty()){
            binding.residence.getEditText().setError("Residence is Required");
            return false;
        } if (binding.date.getEditText().getText().toString().isEmpty()){
            binding.date.getEditText().setError("Date is Required");
            return false;
        } if (Integer.parseInt(binding.date.getEditText().getText().toString()) > 31){
            binding.date.getEditText().setError("Date is not valid");
            return false;
        }  if (Integer.parseInt(binding.date.getEditText().getText().toString()) < 1){
            binding.date.getEditText().setError("Date is not valid");
            return false;
        } if (binding.month.getEditText().getText().toString().isEmpty()){
            binding.month.getEditText().setError("Month is Required");
            return false;
        } if (Integer.parseInt(binding.month.getEditText().getText().toString()) > 12){
            binding.month.getEditText().setError("Month is not valid");
            return false;
        } if (Integer.parseInt(binding.month.getEditText().getText().toString()) < 1){
            binding.month.getEditText().setError("Month is not valid");
            return false;
        } if (binding.year.getEditText().getText().toString().isEmpty()){
            binding.year.getEditText().setError("Year is Required");
            return false;
        } if (binding.year.getEditText().getText().toString().length() < 4){
            binding.year.getEditText().setError("Year is not Valid");
            return false;
        } if (Integer.parseInt(binding.year.getEditText().getText().toString()) > Integer.parseInt(d)) {
            binding.year.getEditText().setError("Year is not Valid");
            return false;
        } if (Integer.parseInt(binding.year.getEditText().getText().toString()) < 1) {
            binding.year.getEditText().setError("Year is not Valid");
            return false;
        } if (binding.email.getEditText().getText().toString().isEmpty()){
            binding.email.getEditText().setError("Email is Required");
            return false;
        } if (!Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()){
            binding.email.getEditText().setError("Email is no valid");
            return false;
        } if (binding.password.getEditText().getText().toString().isEmpty()){
            binding.password.getEditText().setError("Password is Required");
            return false;
        } if (binding.cnfrmPassword.getEditText().getText().toString().isEmpty()){
            binding.cnfrmPassword.getEditText().setError("Password is Required");
            return false;
        } if (!binding.password.getEditText().getText().toString().equals(binding.cnfrmPassword.getEditText().getText().toString())){
            binding.cnfrmPassword.getEditText().setError("Password doesn't match");
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data!=null && data.getData()!= null){
                image = data.getData();
                Glide.with(SignUpActivity.this).load(image).placeholder(R.drawable.profile_icon).into(binding.profileImage);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}