package com.moutamid.hbdresidents.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.hbdresidents.R;
import com.moutamid.hbdresidents.models.ChatModel;
import com.moutamid.hbdresidents.utilis.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.HolderAndroid> {
    private Context context;
    private ArrayList<ChatModel> androidArrayList;

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    public ChatAdapter(Context context, ArrayList<ChatModel> androidArrayList) {
        this.context = context;
        this.androidArrayList = androidArrayList;
    }

    @NonNull
    @Override
    public HolderAndroid onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_LEFT) {
            view = LayoutInflater.from(context).inflate(R.layout.left_chat, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.right_chat, parent, false);
        }
        return new HolderAndroid(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAndroid holder, int position) {
        ChatModel model = androidArrayList.get(position);
        holder.message.setText(model.getMessage());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm aa");
        String time = timeFormat.format(model.getTimestamps());
        holder.time.setText(time);
    }

    @Override
    public int getItemCount() {
        return androidArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Constants.auth().getCurrentUser().getUid().equals(androidArrayList.get(position).getSenderID()) ? MSG_TYPE_RIGHT : MSG_TYPE_LEFT;
    }

    class HolderAndroid extends RecyclerView.ViewHolder {
        TextView message, time;
        HolderAndroid(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
        }
    }
}
