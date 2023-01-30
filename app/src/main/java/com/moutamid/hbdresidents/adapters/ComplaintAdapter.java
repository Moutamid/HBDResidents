package com.moutamid.hbdresidents.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.hbdresidents.R;
import com.moutamid.hbdresidents.models.ComplaintModel;

import java.text.SimpleDateFormat;
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
        ComplaintModel model = list.get(holder.getAdapterPosition());

        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        if (model.getStatus().equals("PEN")){
            holder.status.setText("Pending");
            holder.status.setTextColor(context.getResources().getColor(R.color.white));
            holder.statusCard.setCardBackgroundColor(context.getResources().getColor(R.color.dark));
        } else {
            holder.status.setText("Completed");
            holder.status.setTextColor(context.getResources().getColor(R.color.white));
            holder.statusCard.setCardBackgroundColor(context.getResources().getColor(R.color.primary));
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date = format.format(model.getTimestamp());
        holder.date.setText(date);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ComplaintVH extends RecyclerView.ViewHolder{
        TextView title, desc, date, status;
        CardView statusCard;
        public ComplaintVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            desc = itemView.findViewById(R.id.desc);
            status = itemView.findViewById(R.id.statusTitle);
            statusCard = itemView.findViewById(R.id.status);
        }
    }
}
