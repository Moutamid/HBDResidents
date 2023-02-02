package com.moutamid.hbdresidents;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.fxn.stash.Stash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.moutamid.hbdresidents.adapters.ChatAdapter;
import com.moutamid.hbdresidents.databinding.ActivityChatBinding;
import com.moutamid.hbdresidents.models.ChatModel;
import com.moutamid.hbdresidents.models.UserModel;
import com.moutamid.hbdresidents.utilis.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    Date date;
    String ID, message, title;
    SimpleDateFormat format;
    ChatAdapter adapterChat;
    ArrayList<ChatModel> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        format = new SimpleDateFormat("HH:mm aa");

        chatList = new ArrayList<>();

        ID = Stash.getString("uid");
        message = Stash.getString("message");
        title = Stash.getString("title");

        binding.message.setText(message);

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setHasFixedSize(false);

        binding.back.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setMessage("If you leave the chat you can't enter this chat for complaint about "+ title)
                    .setTitle("Attention")
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        Stash.clear("uid");
                        startActivity(new Intent(this, MyNeighborhoodActivity.class));
                        finish();
                    }).setNegativeButton("No", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    }).show();
        });

        Constants.databaseReference().child("chats").child(Constants.auth().getCurrentUser().getUid())
                .child("69WQh6r25yMtjdPUnWUmBbYrbsV2")
                .child(ID)
                .addChildEventListener(new ChildEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){
                            ChatModel conversation = snapshot.getValue(ChatModel.class);
                            chatList.add(conversation);
                            chatList.sort(Comparator.comparing(ChatModel::getTimestamps));
                            adapterChat = new ChatAdapter(ChatActivity.this, chatList);
                            binding.recycler.setAdapter(adapterChat);
                            binding.recycler.scrollToPosition(chatList.size()-1);
                            adapterChat.notifyItemInserted(chatList.size()-1);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){

                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists()){

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.send.setOnClickListener(v -> {
            if (!binding.message.getText().toString().isEmpty()){
                String msg = binding.message.getText().toString();
                //binding.message.setText("");
                date = new Date();
                ChatModel conversation = new ChatModel(
                        msg,
                        Constants.auth().getCurrentUser().getUid(),
                        date.getTime()
                );
                Constants.databaseReference().child("chats").child(Constants.auth().getCurrentUser().getUid())
                        .child("69WQh6r25yMtjdPUnWUmBbYrbsV2")
                        .child(ID)
                        .push()
                        .setValue(conversation)
                        .addOnSuccessListener(unused -> {
                            ReciverSide();
                        }).addOnFailureListener(e -> {

                        });
            }
        });

    }
    private void ReciverSide() {
        String msg = binding.message.getText().toString();
        date = new Date();
        ChatModel conversation = new ChatModel(
                msg,
                Constants.auth().getCurrentUser().getUid(),
                date.getTime()
        );
        Constants.databaseReference().child("chats").child("69WQh6r25yMtjdPUnWUmBbYrbsV2")
                .child(Constants.auth().getCurrentUser().getUid())
                .child(ID)
                .push()
                .setValue(conversation)
                .addOnSuccessListener(unused -> {
                    binding.message.setText("");
                }).addOnFailureListener(e -> {

                });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("If you leave the chat you can't enter this chat for complaint about "+ title)
                .setTitle("Attention")
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Stash.clear("uid");
                    startActivity(new Intent(this, MyNeighborhoodActivity.class));
                    finish();
                }).setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).show();
    }
}