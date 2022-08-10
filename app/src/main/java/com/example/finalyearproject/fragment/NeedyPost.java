package com.example.finalyearproject.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.finalyearproject.ChiefWall;
import com.example.finalyearproject.Model.Member;
import com.example.finalyearproject.Model.Post;
import com.example.finalyearproject.Model.UserInfo;
import com.example.finalyearproject.NeedyWall;
import com.example.finalyearproject.R;
import com.example.finalyearproject.databinding.FragmentChiefPostBinding;
import com.example.finalyearproject.databinding.FragmentNeedyPostBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NeedyPost extends Fragment {

    private FragmentNeedyPostBinding binding;
    String userId;
    Uri uri;
    Member member;
    FirebaseStorage storage;
    ProgressDialog dialog;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String selecteditem;
    String name;
    String stratdate,enddate;
    final Calendar myCalendar= Calendar.getInstance();
    public NeedyPost() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNeedyPostBinding.inflate(inflater, container, false);
        binding = FragmentNeedyPostBinding.inflate(getLayoutInflater());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getInstance().getReference("Needy");
        storage = FirebaseStorage.getInstance();
        user = mAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        member = new Member();

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH,i1);
                myCalendar.set(Calendar.DAY_OF_MONTH,i2);
                updatestartdate();

            }


        };

        DatePickerDialog.OnDateSetListener date2 =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH,i1);
                myCalendar.set(Calendar.DAY_OF_MONTH,i2);
                updateenddate();

            }


        };
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Needy").child(userId).child("Category");

        List<String> Categories = new ArrayList<>();
        Categories.add(0, "Choose");
        Categories.add("Food");
        Categories.add("Education");
        Categories.add("Wedding");
        Categories.add("Shelter");
        Categories.add("Health");


        ArrayAdapter<String> data;
        data = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Categories);
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(data);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).equals("Choose")){



                } else{
                    selecteditem = adapterView.getItemAtPosition(i).toString();

                }
            }
            public void onNothingSelected(AdapterView<?> adapterView){}
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int id = (int)snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("Needy").child(FirebaseAuth.getInstance().getUid()).child("Personal Info")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            UserInfo user = snapshot.getValue(UserInfo.class);
                            // Picasso.get().load(user.getProfilePicture())
                            // .placeholder(R.drawable.profile).into(binding.ccprofile);
                            name=user.getName();
                            binding.username.setText(user.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.postDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String description = binding.postDescription.getText().toString();
                if (!description.isEmpty()) {
                    binding.postButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.follow_btn_bg));
                    binding.postButton.setTextColor(getContext().getResources().getColor(R.color.white));
                    binding.postButton.setEnabled(true);
                } else {
                    binding.postButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.follow_active_bg));
                    binding.postButton.setTextColor(getContext().getResources().getColor(R.color.black));
                    binding.postButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        binding.addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

     /*   //enable button on  start Date
        binding.startdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String Start_date = binding.startdate.getText().toString();
                if(!Start_date.isEmpty())
                {
                    binding.postButton.setBackgroundColor(Color.parseColor("#2D42B5"));
                    binding.postButton.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.postButton.setEnabled(true);
                }
                else {
                    binding.postButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding.postButton.setTextColor(Color.parseColor("#8F8C8C"));
                    binding.postButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });*/

        //enable button on  end Date
        binding.startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),date2,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
       /* binding.enddate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String End_date = binding.enddate.getText().toString();
                if(!End_date.isEmpty())
                {
                    binding.postButton.setBackgroundColor(Color.parseColor("#2D42B5"));
                    binding.postButton.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.postButton.setEnabled(true);
                }
                else {
                    binding.postButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding.postButton.setTextColor(Color.parseColor("#8F8C8C"));
                    binding.postButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });*/

        //enable button on  amount
        binding.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String Amount = binding.amount.getText().toString();
                if(!Amount.isEmpty())
                {
                    binding.postButton.setBackgroundColor(Color.parseColor("#2D42B5"));
                    binding.postButton.setTextColor(Color.parseColor("#FFFFFF"));
                    binding.postButton.setEnabled(true);
                }
                else {
                    binding.postButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding.postButton.setTextColor(Color.parseColor("#8F8C8C"));
                    binding.postButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                final StorageReference reference = storage.getReference().child("Needy" + "Posts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime() + "");
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Post post = new Post();
                                post.setStart_date(stratdate);
                                post.setEnd_date(enddate);
                                post.setTitle(binding.destitleofpost.getText().toString());
                                //post.setAmount(); //yahann amount bhejni haiiiii yeh waali cheez add kardenaa
                                post.setCategory(selecteditem); // yahaan category
                                post.setPostImage(uri.toString());
                                post.setAmount(binding.amount.getText().toString());
                                post.setPostedBy(FirebaseAuth.getInstance().getUid());
                                post.setPostDescription(binding.postDescription.getText().toString());
                                post.setPostedAt(new Date().getTime());
                                post.setName(name);
                                post.setParentnode("Needy");

                                database.getReference().child("Posts").push().setValue(post);
                                database.getReference().child("Needy").child(userId).child("Post")
                                        .push().setValue(post)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss();
                                                Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getContext(), NeedyWall.class);
                                                startActivity(intent);
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NeedyWall.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }

        private void updatestartdate(){
            String myFormat="MM/dd/yy";
            SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
            stratdate=dateFormat.format(myCalendar.getTime());
            binding.startdate.setText(stratdate);
    }
    private void updateenddate(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        enddate=dateFormat.format(myCalendar.getTime());
        binding.enddate.setText(enddate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            uri = data.getData();
            binding.postImage.setImageURI(uri);
            binding.postImage.setVisibility(View.VISIBLE);

            binding.postButton.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.follow_btn_bg));
            binding.postButton.setTextColor(getContext().getResources().getColor(R.color.white));
            binding.postButton.setEnabled(true);
        } else {
            Intent intent = new Intent(getContext(), NeedyWall.class);
            startActivity(intent);
        }
    }


}