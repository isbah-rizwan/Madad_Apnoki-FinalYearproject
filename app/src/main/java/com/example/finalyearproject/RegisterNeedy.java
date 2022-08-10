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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterNeedy extends AppCompatActivity {

    private EditText Name, Contact, CNIC, Email, Password, Account;
    private Button register;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_needy);

        mAuth = FirebaseAuth.getInstance();

        Name = (EditText) findViewById(R.id.nename);
        Contact = (EditText) findViewById(R.id.necontact);
        Email = (EditText) findViewById(R.id.neemail);
        CNIC = (EditText) findViewById(R.id.necnic);
        Account = (EditText) findViewById(R.id.nedetails);
        Password = (EditText) findViewById(R.id.nepass);

        register = findViewById(R.id.create);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Name.getText().toString().trim();
                String contact = Contact.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String account = Account.getText().toString().trim();
                String cnic = CNIC.getText().toString().trim();

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
                                if(task.isSuccessful()){
                                    UserInfo User = new UserInfo(name, email, contact, account, cnic,"0");

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(User);

                                    FirebaseDatabase.getInstance().getReference("Needy")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("Personal Info").setValue(User)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(RegisterNeedy.this, "User has been successfully registered", Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(getApplicationContext(), NeedyUploadProof.class));
                                                    } else{
                                                        Toast.makeText(RegisterNeedy.this, "User has not registered. Try again!", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(RegisterNeedy.this, "User already exist! Please login", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getApplicationContext(), Needylogin.class));
                                }
                            }
                        });
            }
        });
    }
}