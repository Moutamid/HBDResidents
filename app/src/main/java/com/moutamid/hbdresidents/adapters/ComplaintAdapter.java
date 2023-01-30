package com.moutamid.hbdresidents.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.hbdresidents.R;
import com.moutamid.hbdresidents.models.ComplaintModel;

import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintVH> {
    Context context;
    ArrayList<ComplaintModel> list;

    public ComplaintAdapter(Context context, ArrayList<ComplaintModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ComplaintVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.complaint_card, parent, false);
        return new ComplaintVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ComplaintVH extends RecyclerView.ViewHolder{

        public ComplaintVH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
