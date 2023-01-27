package com.moutamid.hbdresidents.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.hbdresidents.R;
import com.moutamid.hbdresidents.databinding.FragmentHomeBinding;
import com.moutamid.hbdresidents.models.UserModel;
import com.moutamid.hbdresidents.utilis.Constants;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        Description psiDes = new Description();
        psiDes.setText("PSI LEVEL CHART");
        Description denDes = new Description();
        denDes.setText("DENGUE LEVEL CHART");

        BarData data = new BarData(getDataSet());
        binding.psiChart.setData(data);
        binding.psiChart.setDescription(psiDes);
        binding.psiChart.animateXY(2000, 2000);
        binding.psiChart.invalidate();

        binding.dengueChart.setData(data);
        binding.dengueChart.setDescription(denDes);
        binding.dengueChart.animateXY(2000, 2000);
        binding.dengueChart.invalidate();

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


        return view;
    }

    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);
        BarEntry v1e7 = new BarEntry(1000.000f, 6); // Jun
        valueSet1.add(v1e7);
        BarEntry v1e8 = new BarEntry(10.000f, 7); // Jun
        valueSet1.add(v1e8);
        BarEntry v1e9 = new BarEntry(500.000f, 8); // Jun
        valueSet1.add(v1e9);
        BarEntry v1e10 = new BarEntry(100.000f, 9); // Jun
        valueSet1.add(v1e10);
        BarEntry v1e11 = new BarEntry(1.000f, 10); // Jun
        valueSet1.add(v1e11);
        BarEntry v1e12 = new BarEntry(100.000f, 11); // Jun
        valueSet1.add(v1e12);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Level");
        //barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<com.github.mikephil.charting.interfaces.datasets.IBarDataSet>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<String>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JUL");
        xAxis.add("AUG");
        xAxis.add("SEP");
        xAxis.add("OCT");
        xAxis.add("NOV");
        xAxis.add("DEC");
        return xAxis;
    }

}