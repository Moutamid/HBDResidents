package com.moutamid.hbdresidents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.hbdresidents.databinding.ActivityComplaintBinding;
import com.moutamid.hbdresidents.models.ComplaintModel;
import com.moutamid.hbdresidents.models.PendingModel;
import com.moutamid.hbdresidents.utilis.Constants;
import com.moutamid.hbdresidents.utilis.NotificationHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ComplaintActivity extends AppCompatActivity {
    ActivityComplaintBinding binding;
    private static final int PICK_FROM_GALLERY = 1;
    Uri imageURI;
    String type;
    Date d;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");

        type = getIntent().getStringExtra("type");

        d = new Date();
        checkStatus();

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
            if (validate()) {
                progressDialog.show();
                if (imageURI != null) {
                    uploadImageUrgent(true);
                } else {
                    uploadComplaintUrgent("", true);
                }
            }
        });

        binding.back.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        binding.send.setOnClickListener(v -> {
            if (validate()) {
                progressDialog.show();
                if (imageURI != null) {
                    uploadImage(false);
                } else {
                    uploadComplaint("", false);
                }
            }
        });

        binding.addImage.setOnClickListener(v -> {
            if (imageURI != null) {
                showDialog();
            } else {
                getImageFromGallery();
            }
        });
    }

    private void checkStatus() {
        String uid = Stash.getString("uid", "");
        Constants.databaseReference().child("pending").child(Constants.auth().getCurrentUser().getUid())
                .child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            PendingModel pending = snapshot.getValue(PendingModel.class);
                            if (pending.isPending()){
                                NotificationHelper notificationHelper = new NotificationHelper(ComplaintActivity.this);
                                notificationHelper.sendHighPriorityNotification(
                                        "Your Wait is Over",
                                        "You can now chat with the admin",
                                        MyNeighborhoodActivity.class
                                );
                                Intent i = new Intent(ComplaintActivity.this, ChatActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void uploadComplaintUrgent(String image, boolean urgent) {
        String uid = UUID.randomUUID().toString();
        ComplaintModel complaint = new ComplaintModel(
                uid,
                Constants.auth().getCurrentUser().getUid(),
                binding.title.getEditText().getText().toString(),
                binding.desc.getEditText().getText().toString(),
                type,
                image,
                d.getTime(),
                urgent,
                "PEN"
        );
        Constants.databaseReference().child("complaints").child(Constants.auth().getCurrentUser().getUid())
                .child(uid)
                .setValue(complaint).addOnSuccessListener(unused -> {
                    waiting(uid);
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void waiting(String uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("pending", false);
        Constants.databaseReference().child("pending").child(Constants.auth().getCurrentUser().getUid())
                .child(uid).setValue(map).addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Stash.put("uid", uid);
                    Stash.put("message", binding.desc.getEditText().getText().toString());
                    Stash.put("title", binding.title.getEditText().getText().toString());
                    new AlertDialog.Builder(ComplaintActivity.this)
                            .setMessage("Your on a waiting list we will notify you when your turn comes.")
                            .setTitle("Please Wait")
                            .setPositiveButton("Ok", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            }).show();
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadImageUrgent(boolean urgent) {
        Constants.storageReference(Constants.auth().getCurrentUser().getUid())
                .child("complaints").child(Constants.auth().getCurrentUser().getUid() + d.getTime())
                .putFile(imageURI).addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadComplaintUrgent(uri.toString(), urgent);
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(ComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validate() {
        if (binding.title.getEditText().getText().toString().isEmpty()){
            binding.title.getEditText().setError("Please add Title");
            binding.title.getEditText().requestFocus();
            return false;
        }
        if (binding.desc.getEditText().getText().toString().isEmpty()){
            binding.desc.getEditText().setError("Please add Description");
            binding.desc.getEditText().requestFocus();
            return false;
        }
        return true;
    }

    private void uploadImage(boolean urgent) {
        Constants.storageReference(Constants.auth().getCurrentUser().getUid())
                .child("complaints").child(Constants.auth().getCurrentUser().getUid() + d.getTime())
                .putFile(imageURI).addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        uploadComplaint(uri.toString(), urgent);
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(ComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadComplaint(String image, boolean urgent) {
        String uid = UUID.randomUUID().toString();
        ComplaintModel complaint = new ComplaintModel(
                uid,
                Constants.auth().getCurrentUser().getUid(),
                binding.title.getEditText().getText().toString(),
                binding.desc.getEditText().getText().toString(),
                type,
                image,
                d.getTime(),
                urgent,
                "PEN"
        );
        Constants.databaseReference().child("complaints").child(Constants.auth().getCurrentUser().getUid())
                .child(uid)
                .setValue(complaint).addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    finish();
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        checkStatus();
    }
}