package com.example.finalyearproject.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.finalyearproject.Adapter.PostAdapter;
import com.example.finalyearproject.Model.Post;
import com.example.finalyearproject.R;
import com.example.finalyearproject.Utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubHome extends Fragment {

    String userId;
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Post> postslist;
    EditText searchtext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_home, container, false);
        recyclerView = view.findViewById(R.id.adashboard);
     //   searchtext = view.findViewById(R.id.searchtext);

        postslist=new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();


        databaseReference = firebaseDatabase.getReference("Needy");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                    for (DataSnapshot d: userSnapshot.child("Post").getChildren())
                    {
                        Post post = d.getValue(Post.class);
                        post.setParentnode("Needy");
                        postslist.add(post);
                    }
                }

                databaseReference = firebaseDatabase.getReference("Chief Donor");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                            for (DataSnapshot d: userSnapshot.child("Post").getChildren())
                            {
                                Post post = d.getValue(Post.class);
                                post.setParentnode("Chief Donor");
                                postslist.add(post);
                            }
                        }

                        databaseReference = firebaseDatabase.getReference("Sub Donor");

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                                    for (DataSnapshot d: userSnapshot.child("Post").getChildren())
                                    {
                                        Post post = d.getValue(Post.class);
                                        post.setParentnode("Sub Donor");

                                        postslist.add(post);
                                    }
                                }
                                 postAdapter = new PostAdapter(postslist, getActivity(),"Sub Donor", new PostAdapter.OnClickListnener() {
                                    @Override
                                    public void onitemclick() {
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new PaymentFragment()).commit();

                                    }
                                });
                                recyclerView.setAdapter(postAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setHasFixedSize(true);

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

      //  searchKeyword();

        return view;
    }

    private void searchKeyword() {
        searchtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("mytext",charSequence.toString());
                searchuser(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    public void searchuser(String queryText) {
        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("name")
                .startAt(queryText)
                .endAt(queryText + "\uf8ff");
        postslist.clear();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Utils.e("dataSnapshot", "" + dataSnapshot.getKey());
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Post post= eventSnapshot.getValue(Post.class);
                    postslist.add(post);
                }
                postAdapter = new PostAdapter(postslist,getActivity(),"Sub Donor", new PostAdapter.OnClickListnener() {
                    @Override
                    public void onitemclick() {
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new PaymentFragment()).commit();

                    }
                });

                recyclerView.setAdapter(postAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // searchedUserlist = new ArrayList<>();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();

    }
}