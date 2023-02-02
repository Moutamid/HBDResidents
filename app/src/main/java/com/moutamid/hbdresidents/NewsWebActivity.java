package com.moutamid.hbdresidents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.moutamid.hbdresidents.databinding.ActivityNewsWebBinding;
import com.moutamid.hbdresidents.utilis.MyBrowser;

public class NewsWebActivity extends AppCompatActivity {
    ActivityNewsWebBinding binding;
    String url, title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");

        binding.toolbar.setTitle(title);

        binding.web.setWebViewClient(new MyBrowser());
        binding.web.getSettings().setLoadsImagesAutomatically(true);
        binding.web.getSettings().setJavaScriptEnabled(true);
        binding.web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.web.loadUrl(url);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MyNeighborhoodActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}