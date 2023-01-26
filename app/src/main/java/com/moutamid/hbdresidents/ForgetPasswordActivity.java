package com.moutamid.hbdresidents;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.google.firebase.auth.ActionCodeSettings;
import com.moutamid.hbdresidents.databinding.ActivityForgetPasswordBinding;
import com.moutamid.hbdresidents.utilis.Constants;

public class ForgetPasswordActivity extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.send.setOnClickListener(v -> {
            if (binding.email.getEditText().getText().toString().isEmpty() ||
                    !Patterns.EMAIL_ADDRESS.matcher(binding.email.getEditText().getText().toString()).matches()){
                Toast.makeText(this, "Please Enter a valid Email", Toast.LENGTH_SHORT).show();
            } else {
                Constants.auth().sendPasswordResetEmail(
                        binding.email.getEditText().getText().toString()
                ).addOnSuccessListener(unused -> {
                    binding.email.getEditText().setText("");
                    Toast.makeText(this, "Check Your Email", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}