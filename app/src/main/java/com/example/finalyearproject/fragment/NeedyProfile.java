package com.example.finalyearproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.ChiefWall;
import com.example.finalyearproject.Model.UserInfo;
import com.example.finalyearproject.NeedyWall;
import com.example.finalyearproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NeedyProfile extends Fragment {

    TextView Name, Contact, CNIC, Email, Account;
    String userId, name, email, contact, cnic, account;
    ImageView back;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseUser user;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_needy_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getInstance().getCurrentUser();
        databaseReference = database.getInstance().getReference("Needy");
        userId = user.getUid();

        Name = view.findViewById(R.id.nedname);
        Contact = view.findViewById(R.id.nedcontact);
        Email = view.findViewById(R.id.nedemail);
        CNIC = view.findViewById(R.id.nedcnic);
        Account = view.findViewById(R.id.neddetails);

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NeedyWall.class);
                startActivity(intent);
            }
        });

        databaseReference.child(userId).child("Personal Info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userprofile = snapshot.getValue(UserInfo.class);

                if(userprofile != null){
                    name = userprofile.getName();
                    contact = userprofile.getContact();
                    email = userprofile.getEmail();
                    account = userprofile.getAccount();
                    cnic = userprofile.getCnic();

                    Name.setText(name);
                    Contact.setText(contact);
                    Email.setText(email);
                    CNIC.setText(cnic);
                    Account.setText(account);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}