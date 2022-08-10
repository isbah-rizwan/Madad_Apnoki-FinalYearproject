package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.Model.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChiefNeedCategory extends AppCompatActivity {

    private TextView text;
    private Button category, next;
    private Spinner spinner;
    FirebaseDatabase FbDb;
    DatabaseReference DbRef;
    Member member;
    private String userId;
    FirebaseUser user;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chief_need_category);

        text = findViewById(R.id.text);
        spinner = findViewById(R.id.spinner);
        category = findViewById(R.id.category);

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.finalyearproject.ChiefNeedCategory.this, ChiefWall.class);
                startActivity(intent);
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        member = new Member();
        DbRef = FbDb.getInstance().getReference("Chief Donor").child(userId).child("Category");

        List<String> Categories = new ArrayList<>();
        Categories.add(0, "Choose");
        Categories.add("Food");
        Categories.add("Education");
        Categories.add("Wedding");
        Categories.add("Shelter");
        Categories.add("Health");

        ArrayAdapter<String> data;
        data = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Categories);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(data);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).equals("Choose")){

                } else{
                    text.setText(adapterView.getSelectedItem().toString());
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView){}
        });

        DbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    id = (int)snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                member.setSpinner(spinner.getSelectedItem().toString());
                Toast.makeText(com.example.finalyearproject.ChiefNeedCategory.this,
                        "Category Saved!", Toast.LENGTH_SHORT).show();

                DbRef.child(String.valueOf(id+1)).setValue(member);
            }
        });
    }
}