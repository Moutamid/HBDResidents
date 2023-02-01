package com.moutamid.hbdresidents.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.hbdresidents.R;
import com.moutamid.hbdresidents.adapters.ComplaintAdapter;
import com.moutamid.hbdresidents.databinding.FragmentComplaintBinding;
import com.moutamid.hbdresidents.models.ComplaintModel;
import com.moutamid.hbdresidents.utilis.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ComplaintFragment extends Fragment {
    FragmentComplaintBinding binding;
    Context context;
    ArrayList<ComplaintModel> list;
    ComplaintAdapter adapter;

    public ComplaintFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentComplaintBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        list = new ArrayList<>();

        binding.recycler.setHasFixedSize(false);
        binding.recycler.setLayoutManager(new LinearLayoutManager(context));

        Constants.databaseReference().child("complaints").child(Constants.auth().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            ComplaintModel model = snapshot1.getValue(ComplaintModel.class);
                            if (model.getType().equals("COMP")){
                                list.add(model);
                            }
                        }
                        Collections.sort(list, Comparator.comparing(ComplaintModel::getTimestamp));
                        Collections.reverse(list);
                        adapter = new ComplaintAdapter(context, list);
                        binding.recycler.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }
}