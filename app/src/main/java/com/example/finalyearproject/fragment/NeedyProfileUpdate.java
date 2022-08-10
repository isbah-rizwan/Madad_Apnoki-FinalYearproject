package com.example.finalyearproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalyearproject.Model.UserInfo;
import com.example.finalyearproject.NeedyWall;
import com.example.finalyearproject.R;
import com.example.finalyearproject.SubWall;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NeedyProfileUpdate extends Fragment {

    EditText Name, Contact, CNIC, Email, Account;
    Button Update;
    ImageView back;
    String userId, name, email, contact, cnic, account;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseUser user;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_needy_profile_update, container, false);

        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NeedyWall.class);
                startActivity(intent);
            }
        });

        Update = view.findViewById(R.id.update);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNameChange() || isContactChange() || isCnicChange() || isEmailChange() || isAccountChange()) {
                    Toast.makeText(getContext(), "You have update your profile", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Profile can not be update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getInstance().getCurrentUser();
        databaseReference = database.getInstance().getReference("Needy");
        userId = user.getUid();

        Name = view.findViewById(R.id.nsname);
        Contact = view.findViewById(R.id.nscontact);
        Email = view.findViewById(R.id.nsemail);
        CNIC = view.findViewById(R.id.nscnic);
        Account = view.findViewById(R.id.nsdetails);

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

    private boolean isNameChange(){
        if(!name.equals(Name.getText().toString())){
            databaseReference.child(userId).child("name").setValue(Name.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isContactChange(){
        if(!contact.equals(Contact.getText().toString())){
            databaseReference.child(userId).child("contact").setValue(Contact.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailChange(){
        if(!email.equals(Email.getText().toString())){
            databaseReference.child(userId).child("email").setValue(Email.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isCnicChange(){
        if(!cnic.equals(CNIC.getText().toString())){
            databaseReference.child(userId).child("cnic").setValue(CNIC.getText().toString());
            return true;
        } else {
            return false;
        }
    }

    private boolean isAccountChange(){
        if(!account.equals(Account.getText().toString())){
            databaseReference.child(userId).child("account").setValue(Account.getText().toString());
            return true;
        } else {
            return false;
        }
    }
}