package com.example.finalyearproject.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.Model.Transactions;
import com.example.finalyearproject.Model.UserInfo;
import com.example.finalyearproject.R;
import com.example.finalyearproject.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentFragment extends Fragment {

    EditText cnic,amount;
    Dialog progressDialog;
    Button paynow;
    FirebaseDatabase firebaseDatabase;
    String cnic_,currentbslnc;
    DatabaseReference databaseReference;
    TextView currentblnc;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_payment, container, false);
        initviews();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userInfo=snapshot.getValue(UserInfo.class);
                cnic_ =userInfo.getCnic();
                currentbslnc=userInfo.getAmount();
                currentblnc.setText("Current Balance: "+userInfo.getAmount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isvalidated()) {
                    Utils.showDialog(progressDialog);
                    Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("cnic").equalTo(cnic.getText().toString().trim());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.exists())
                            {UserInfo userInfo = null;
                            String key = null;
                                for (DataSnapshot child: snapshot.getChildren()) {
                                    //depositing in to reciever accouunt
                                    userInfo=child.getValue(UserInfo.class);
                                    key=child.getKey();

                                    int finalamount_reciever= Integer.parseInt(userInfo.getAmount())+Integer.parseInt(amount.getText().toString().trim());
                                    Handler handler=new Handler();
                                    //reciever key
                                    String finalKey = key;
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(finalKey).child("amount").setValue(""+finalamount_reciever);

                                            int finalamount_sender =Integer.parseInt(currentbslnc)-Integer.parseInt(amount.getText().toString().trim());
                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("amount").setValue(""+finalamount_sender);

                                            Toast.makeText(getContext(),"Amount has successfully been transfered",Toast.LENGTH_SHORT).show();
                                            Utils.dismissDialog(progressDialog);

                                            Transactions transactions=new Transactions();
                                            transactions.setReciever(finalKey);
                                            transactions.setSentfrom(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            transactions.setAmount(amount.getText().toString().trim());
                                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                            transactions.setDate(date);

                                            //pushing in reciever node
                                            FirebaseDatabase.getInstance().getReference("Transactions")
                                                    .child(finalKey).push().setValue(transactions);
                                            // pushing in sender node
                                            FirebaseDatabase.getInstance().getReference("Transactions")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(transactions);

                                            cnic.setText("");
                                            amount.setText("");


                                        }
                                    },2000);
                                }
                            }
                            else {
                                Toast.makeText(getActivity(),"This user doesnot exist in system",Toast.LENGTH_SHORT).show();
                                Utils.dismissDialog(progressDialog);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
            });

        return view;
    }


    private void initviews() {
        cnic=view.findViewById(R.id.cnic);
        amount=view.findViewById(R.id.amount);
        paynow=view.findViewById(R.id.paynow);
        currentblnc=view.findViewById(R.id.currentblnc);
        progressDialog= Utils.progressDialog(getActivity());
    }
    private boolean isvalidated() {
    boolean isvalidate=false;

    if(!cnic.getText().toString().equals(""))
    {
        if(!amount.getText().toString().equals(""))
        {
            if(cnic.getText().toString().length()==13)
            {
                if(Integer.parseInt(amount.getText().toString())>0)
                {
                    if(!(cnic_).equals(cnic.getText().toString().trim()))
                    {
                        Integer current_balance=Integer.parseInt(currentbslnc);
                        Integer amount_=Integer.parseInt(amount.getText().toString());

                        try{
                            if(current_balance>amount_)
                            {
                                isvalidate=true;
                            }
                            else {
                                Toast.makeText(getActivity(),"You cannot transfer this amount as this amount exceeds your current balance",Toast.LENGTH_SHORT).show();
                            }

                        }catch (NumberFormatException e)
                        {
                           Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();

                        }

                    }
                    else {
                        Toast.makeText(getActivity(),"You cannot transfer into your own account",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    amount.setError("Minimum amount to sent is Rs.1");
                }
            }
            else {
                cnic.setError("CNIC must be a (13 digits) no");

            }
        }
        else {
            cnic.setError("Please enter the amount to sent");
        }
    }
    else {
        cnic.setError("Please enter the CNIC No");
    }
    return isvalidate;
    }


}