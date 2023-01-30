package com.moutamid.hbdresidents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moutamid.hbdresidents.databinding.ActivityComplaintBinding;

public class ComplaintActivity extends AppCompatActivity {
    ActivityComplaintBinding binding;
    private static final int PICK_FROM_GALLERY = 1;
    Uri imageURI;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        type = getIntent().getStringExtra("type");

        if (type.equals("FEED")){
            binding.header.setText("We Love Your Feedback!");
            binding.title.setHint("Feedback Title");
            binding.desc.setHint("Feedback Description");
            binding.urgent.setVisibility(View.GONE);
        } else {
            binding.header.setText("Have Any Complaint?");
            binding.title.setHint("Complaint Title");
            binding.desc.setHint("Complaint Description");
            binding.urgent.setVisibility(View.VISIBLE);
        }

        binding.urgent.setOnClickListener(v -> {
            Intent i = new Intent(this, ChatActivity.class);
            i.putExtra("title", binding.title.getEditText().getText().toString());
            i.putExtra("desc", binding.desc.getEditText().getText().toString());
            startActivity(i);
        });

        binding.back.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        binding.send.setOnClickListener(v -> {

        });

        binding.addImage.setOnClickListener(v -> {
            if (imageURI != null) {
                showDialog();
            } else {
                getImageFromGallery();
            }
        });

    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.show_image_dialog);

        TextView delete = (TextView) dialog.findViewById(R.id.delete_image);
        TextView change = (TextView) dialog.findViewById(R.id.change_image);
        TextView close = (TextView) dialog.findViewById(R.id.close_image);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.preview_image);

        imageView.setImageURI(imageURI);

        delete.setOnClickListener(v -> {
            imageURI = null;
            Glide.with(ComplaintActivity.this).load(R.drawable.add_image).into(binding.addImage);
            dialog.dismiss();
        });

        change.setOnClickListener(v -> {
            getImageFromGallery();
            dialog.dismiss();
        });

        close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Continue with"), PICK_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_FROM_GALLERY) {
                try {
                    if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                        imageURI = data.getData();
                        binding.addImage.setImageURI(imageURI);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}