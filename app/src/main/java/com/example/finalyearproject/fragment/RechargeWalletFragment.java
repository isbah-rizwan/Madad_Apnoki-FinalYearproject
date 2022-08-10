package com.example.finalyearproject.fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.Model.UserInfo;
import com.example.finalyearproject.R;
import com.example.finalyearproject.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RechargeWalletFragment extends Fragment {

    View view;
    String userId;
    TextView currentblnc;
    EditText amount;
    Button recharge;
    FirebaseUser user;
    String amount_;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String currentbslnc;
    Dialog progressdialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_recharge_wallet, container, false);
        initviews();
        fetchAmount();

        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isvalidated())
                {
                    Utils.showDialog(progressdialog);
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            amount_=""+(Integer.parseInt(currentbslnc)+Integer.parseInt(amount.getText().toString().trim()));
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("amount").setValue(amount_);
                            Utils.dismissDialog(progressdialog);
                            amount.setText("");
                            Toast.makeText(getContext(),"Successfully recharged amount ",Toast.LENGTH_SHORT).show();

                        }
                    },3000);
                      }


            }
        });
        return  view;
    }

    private void fetchAmount() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userInfo=snapshot.getValue(UserInfo.class);
                currentbslnc=userInfo.getAmount();
                currentblnc.setText("Current Balance: "+currentbslnc);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void initviews() {
        currentblnc=view.findViewById(R.id.currentblnc);
        amount=view.findViewById(R.id.amount);
        recharge=view.findViewById(R.id.recharge);
        progressdialog= Utils.progressDialog(getActivity());
    }

    private boolean isvalidated() {
        boolean isvalidate=false;

            if(!amount.getText().toString().equals("")) {
                isvalidate=true; }
        else {
            amount.setError("Please enter the amount");
        }
        return isvalidate;
    }

}
