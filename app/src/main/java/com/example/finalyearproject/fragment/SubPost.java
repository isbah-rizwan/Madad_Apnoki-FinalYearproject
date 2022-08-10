package com.example.finalyearproject.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.finalyearproject.ChiefWall;
import com.example.finalyearproject.Model.Post;
import com.example.finalyearproject.Model.UserInfo;
import com.example.finalyearproject.R;
import com.example.finalyearproject.SubWall;
import com.example.finalyearproject.databinding.FragmentChiefPostBinding;
import com.example.finalyearproject.databinding.FragmentSubPostBinding;
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

import java.util.Date;

public class SubPost extends Fragment {

    private FragmentSubPostBinding binding;
    String userId;
    Uri uri;
    FirebaseStorage storage;
    ProgressDialog dialog;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String name;

    public SubPost() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSubPostBinding.inflate(inflater, container, false);
        binding = FragmentSubPostBinding.inflate(getLayoutInflater());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getInstance().getReference("Sub Donor");
        storage = FirebaseStorage.getInstance();
        user = mAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        dialog = new ProgressDialog(getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        database.getReference().child("Sub Donor").child(FirebaseAuth.getInstance().getUid()).child("Personal Info")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            UserInfo user = snapshot.getValue(UserInfo.class);
                            // Picasso.get().load(user.getProfilePicture())
                            // .placeholder(R.drawable.profile).into(binding.ccprofile);
                            binding.ssusername.setText(user.getName());
                            name=user.getName();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.sspostDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String description = binding.sspostDescription.getText().toString();
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

        binding.postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                final StorageReference reference = storage.getReference().child("Sub Donor" + "Posts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime() + "");

                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Post post = new Post();
                                post.setPostImage(uri.toString());
                                post.setPostedBy(FirebaseAuth.getInstance().getUid());
                                post.setPostDescription(binding.sspostDescription.getText().toString());
                                post.setPostedAt(new Date().getTime());
                                post.setName(name);
                                post.setParentnode("Sub Donor");

                                database.getReference().child("Posts").push().setValue(post);
                                database.getReference().child("Sub Donor").child(userId).child("Post")
                                        .push().setValue(post)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss();
                                                Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getContext(), SubWall.class);
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
                Intent intent = new Intent(getContext(), SubWall.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
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
            Intent intent = new Intent(getContext(), ChiefWall.class);
            startActivity(intent);
        }
    }
}