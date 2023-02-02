package com.moutamid.hbdresidents.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.hbdresidents.NewsWebActivity;
import com.moutamid.hbdresidents.R;
import com.moutamid.hbdresidents.models.NewsModel;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsVH> {
    Context context;
    ArrayList<NewsModel> list;

    public NewsAdapter(Context context, ArrayList<NewsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NewsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_letter, parent, false);
        return new NewsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsVH holder, int position) {
        NewsModel newsModel = list.get(position);
        holder.title.setText(newsModel.getName());
        holder.desc.setText(newsModel.getDescription());
        Glide.with(context).load(newsModel.getUrlToImage()).into(holder.newsImage);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Instant instant = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            instant = Instant.parse( newsModel.getPublishedAt() );
            Date date = Date.from(instant);
            String d = dateFormat.format(date);
            holder.date.setText(d);
        }

        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(context, NewsWebActivity.class);
            i.putExtra("url", newsModel.getUrl());
            i.putExtra("title", newsModel.getName());
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class NewsVH extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView title, desc, date;
        public NewsVH(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.newsImage);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            date = itemView.findViewById(R.id.date);
        }
    }

}
