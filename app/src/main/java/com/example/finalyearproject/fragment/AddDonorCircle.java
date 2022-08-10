package com.example.finalyearproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.finalyearproject.ChiefWall;
import com.example.finalyearproject.DonorCircle;
import com.example.finalyearproject.R;

public class AddDonorCircle extends Fragment {

    Button subregister;
    ImageView back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_donor_circle, container, false);
        subregister = view.findViewById(R.id.subbutton);
        subregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DonorCircle.class);
                startActivity(intent);
            }
        });

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChiefWall.class);
                startActivity(intent);
            }
        });

        return view;
    }
}