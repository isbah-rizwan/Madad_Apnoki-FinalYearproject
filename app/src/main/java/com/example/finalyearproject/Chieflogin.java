package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Chieflogin extends AppCompatActivity implements View.OnClickListener {

    private EditText Email, Password;
    private TextView register, Forgotpass;
    private Button Login;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chieflogin);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        Login = findViewById(R.id.login);

        Forgotpass = (TextView) findViewById(R.id.forgotPass);
        Forgotpass.setOnClickListener(this);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginuser();
            }
        });
    }

    private void loginuser() {
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Enter your valid email");
            Email.requestFocus();
            return;
        }

        if(password.isEmpty() || password.length() < 8){
            Password.setError("Password has to be greater than 8 characters");
            Password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(com.example.finalyearproject.Chieflogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(com.example.finalyearproject.Chieflogin.this, ChiefWall.class));
                }
                else {
                    Toast.makeText(com.example.finalyearproject.Chieflogin.this, "Please Check Your login Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterChiefdonor.class));
                break;

            case R.id.forgotPass:
                startActivity(new Intent(this, ForgotPass.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null) {
            Intent intent = new Intent(Chieflogin.this, ChiefWall.class);
            startActivity(intent);
        }
    }
}