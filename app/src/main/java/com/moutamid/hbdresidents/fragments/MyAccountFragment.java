package com.moutamid.hbdresidents.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moutamid.hbdresidents.R;
import com.moutamid.hbdresidents.databinding.FragmentMyAccountBinding;

public class MyAccountFragment extends Fragment {
    FragmentMyAccountBinding binding;
    Context context;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();



        return view;
    }
}