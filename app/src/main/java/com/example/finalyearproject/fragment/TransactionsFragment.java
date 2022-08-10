package com.example.finalyearproject.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalyearproject.Adapter.TransactionAdapter;
import com.example.finalyearproject.Model.Transactions;
import com.example.finalyearproject.Model.UserInfo;
import com.example.finalyearproject.R;
import com.example.finalyearproject.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TransactionsFragment extends Fragment {

    View view;
    ArrayList<Transactions> list;
    TransactionAdapter transactionAdapter;
    RecyclerView recycler;
    TextView noDataView,currentbalance;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Dialog progressdialog;
    String currentbslnc;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_transactions, container, false);
        initviews();
        fetchAmount();
        getTransactionlist();
        return  view;
    }

    private void fetchAmount() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userInfo = snapshot.getValue(UserInfo.class);
                currentbslnc = userInfo.getAmount();
                currentbalance.setText("Current Balance: " + currentbslnc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initviews() {
        list=new ArrayList<>();
        recycler=view.findViewById(R.id.recycler);
        noDataView=view.findViewById(R.id.noDataView);
        currentbalance=view.findViewById(R.id.currentbalance);
        progressdialog=Utils.progressDialog(getActivity());
    }

    private void getTransactionlist() {
        Utils.showDialog(progressdialog);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Transactions")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childsnapshot:snapshot.getChildren())
                {
                    Transactions transactions=childsnapshot.getValue(Transactions.class);
                    databaseReference=firebaseDatabase.getReference("Users")
                            .child(transactions.getReciever());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserInfo userInfo=snapshot.getValue(UserInfo.class);
                            transactions.setReciever_name(userInfo.getName());
                            databaseReference=firebaseDatabase.getReference("Users")
                                    .child(transactions.getSentfrom());
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    UserInfo userInfo=snapshot.getValue(UserInfo.class);
                                    transactions.setSender_name(userInfo.getName());
                                    list.add(transactions);
                                    Utils.dismissDialog(progressdialog);
                                    listavaialbe();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            listavaialbe();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
                listavaialbe();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private boolean listavaialbe()
    {

        boolean size=false;
        if (list.size() == 0||list==null) {
            Utils.dismissDialog(progressdialog);
            noDataView.setVisibility(View.VISIBLE);

        } else {
            noDataView .setVisibility(View.GONE);
            setAdapter();
            size=true;

        }
        return size;

    }
    private void setAdapter() {
        transactionAdapter= new TransactionAdapter(list, getActivity());
        recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(transactionAdapter);
    }
}