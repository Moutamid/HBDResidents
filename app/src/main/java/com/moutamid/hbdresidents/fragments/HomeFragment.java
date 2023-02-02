package com.moutamid.hbdresidents.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.hbdresidents.R;
import com.moutamid.hbdresidents.adapters.NewsAdapter;
import com.moutamid.hbdresidents.databinding.FragmentHomeBinding;
import com.moutamid.hbdresidents.models.NewsModel;
import com.moutamid.hbdresidents.models.UserModel;
import com.moutamid.hbdresidents.utilis.Constants;
import com.moutamid.hbdresidents.utilis.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    Context context;
    RequestQueue requestQueue;
    ArrayList<NewsModel> newsList;
    NewsAdapter adapter;

    private static final int MAX_X_VALUE = 12;
    private static final int MAX_Y_VALUE = 50;
    private static final int MIN_Y_VALUE = 5;
    private static final String SET_LABEL = "PSI Levels";
    private static final String[] DAYS = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        newsList = new ArrayList<>();

        requestQueue = VolleySingleton.getmInstance(context).getRequestQueue();

        binding.newsRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.newsRC.setHasFixedSize(false);

        /*BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);*/

        Constants.databaseReference().child("users").child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel model = snapshot.getValue(UserModel.class);
                        Glide.with(context).load(model.getImage()).placeholder(R.drawable.profile_icon).into(binding.profile);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        showPsiChart();
        showDenguChart();

        getNews();

        return view;
    }

    private void configureChartAppearance() {
        binding.psiChart.getDescription().setEnabled(false);
        binding.psiChart.setDrawValueAboveBar(false);

        XAxis xAxis = binding.psiChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return DAYS[(int) value];
            }
        });

        YAxis axisLeft = binding.psiChart.getAxisLeft();
        axisLeft.setGranularity(10f);
        axisLeft.setAxisMinimum(0);

        YAxis axisRight = binding.psiChart.getAxisRight();
        axisRight.setGranularity(10f);
        axisRight.setAxisMinimum(0);
    }

    private BarData createChartData() {
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < MAX_X_VALUE; i++) {
            float x = i;
            Random r = new Random();
            float y = MIN_Y_VALUE + r.nextFloat() * (MIN_Y_VALUE - MAX_Y_VALUE);
            values.add(new BarEntry(x, y));
        }

        BarDataSet set1 = new BarDataSet(values, SET_LABEL);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        return data;
    }

    private void prepareChartData(BarData data) {
        data.setValueTextSize(12f);
        binding.psiChart.setData(data);
        binding.psiChart.invalidate();
    }

    private void getNews() {
//        String url = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=d9ab5f282fec44f8b6d351833b2706b0";
        String url = "https://saurav.tech/NewsAPI/top-headlines/category/health/us.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("articles");
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONObject source = object.getJSONObject("source");
                    NewsModel model = new NewsModel();
                    model.setId(source.getString("id"));
                    model.setName(source.getString("name"));
                    model.setAuthor(object.getString("author"));
                    model.setTitle(object.getString("title"));
                    model.setDescription(object.getString("description"));
                    model.setUrl(object.getString("url"));
                    model.setUrlToImage(object.getString("urlToImage"));
                    model.setPublishedAt(object.getString("publishedAt"));
                    model.setContent(object.getString("content"));
                    newsList.add(model);
                }
                adapter = new NewsAdapter(context, newsList);
                binding.newsRC.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Log.d("Testing123", ""+error.getLocalizedMessage());
            throw new RuntimeException(error);
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void showPsiChart() {
        ArrayList<Double> valueList = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "PSI Level";

        //input data
        for (int i = 0; i < 6; i++) {
            valueList.add(i * 100.1);
        }

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        Collections.shuffle(entries);

        BarDataSet barDataSet = new BarDataSet(entries, title);

        BarData data = new BarData(barDataSet);
        binding.psiChart.setData(data);
        binding.psiChart.invalidate();
    }

    private void showDenguChart() {
        ArrayList<Double> valueList = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "Dengue Level";

        //input data
        for (int i = 0; i < 6; i++) {
            valueList.add(i * 100.1);
        }

        //fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
            entries.add(barEntry);
        }

        Collections.shuffle(entries);

        BarDataSet barDataSet = new BarDataSet(entries, title);

        BarData data = new BarData(barDataSet);
        binding.dengueChart.setData(data);
        binding.dengueChart.invalidate();
    }

}