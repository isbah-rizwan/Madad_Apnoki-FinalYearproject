package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalyearproject.Model.UserInfo;
import com.example.finalyearproject.fragment.ChiefHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonorCircle extends AppCompatActivity {

    private EditText Name, Contact, CNIC, Email, Password, Account;
    String name, contact, email, password, account, cnic;

    private Button register;

    String userId;
    FirebaseDatabase database;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_circle);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        user = mAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        reference = FirebaseDatabase.getInstance().getReference();

        Name = findViewById(R.id.dc_name);
        Contact = findViewById(R.id.dc_contact);
        Email = findViewById(R.id.dc_email);
        CNIC = findViewById(R.id.dc_cnic);
        Account = findViewById(R.id.dc_details);
        Password = findViewById(R.id.dc_pass);

        register = findViewById(R.id.create);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterUser();
            }
        });
    }

    private void RegisterUser() {

        name = Name.getText().toString().trim();
        contact = Contact.getText().toString().trim();
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        account = Account.getText().toString().trim();
        cnic = CNIC.getText().toString().trim();

        if(name.isEmpty()){
            Name.setError("Enter your Name");
            Name.requestFocus();
            return;
        }

        if(contact.isEmpty() || contact.length() != 11){
            Contact.setError("Enter your correct contact");
            Contact.requestFocus();
            return;
        }

        if(cnic.isEmpty() || cnic.length() != 13){
            CNIC.setError("Enter your correct CNIC");
            CNIC.requestFocus();
            return;
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Enter your valid email");
            Email.requestFocus();
            return;
        }

        if(account.isEmpty()){
            Account.setError("Enter your account number");
            Account.requestFocus();
            return;
        }

        if(password.isEmpty() || password.length() < 8){
            Password.setError("Enter password");
            Password.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            UserInfo User = new UserInfo(name, email, cnic, contact, account,"0");

                            database.getInstance().getReference("Chief Donor").child(userId)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("Donor Circle").child("Sub Donor")
                                    .setValue(User).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(DonorCircle.this, "User has been successfully registered", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), ChiefHome.class));
                                    } else {
                                        Toast.makeText(DonorCircle.this, "User has not registered. Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(DonorCircle.this, "User already exist!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), ChiefHome.class));
                        }
                    }
                });

    }
}