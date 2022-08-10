package com.example.finalyearproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalyearproject.Model.Post;
import com.example.finalyearproject.Model.UserInfo;
import com.example.finalyearproject.R;
import com.example.finalyearproject.databinding.DonordashboardBinding;
import com.example.finalyearproject.fragment.PaymentFragment;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {

    ArrayList<Post> list;
    Context context;
    String userId;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference1;
    Fragment fragment;
    OnClickListnener onclicklistener;
    String name;
    String from;


    public PostAdapter(ArrayList<Post> list, Context context,String from,OnClickListnener onclicklistener){
        this.list = list;
        this.context = context;
        this.fragment=fragment;
        this.onclicklistener=onclicklistener;
        this.from=from;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donordashboard, parent, false);

        return new viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull com.example.finalyearproject.Adapter.PostAdapter.viewHolder holder, int position) {

        Post model = list.get(position);
        Picasso.get().load(model.getPostImage()).placeholder(R.drawable.no_image).into(holder.binding.postImg);
        holder.binding.postdescrp.setText(model.getPostDescription());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase.getReference("Users").child(model.getPostedBy());
        databaseReference = firebaseDatabase.getReference(""+model.getParentnode()).child(model.getPostedBy()).child("Personal Info");

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    UserInfo userInfo = snapshot.getValue(UserInfo.class);
                    name=userInfo.getName();
                }
                catch (NullPointerException user)
                {
                    name="user";

                }

            }
                @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                    if (model.getParentnode().equals("Needy")) {

                        if(from.equals("Needy"))
                        {
                            holder.binding.needyextrafields.setVisibility(View.VISIBLE);
                            holder.binding.paynow.setVisibility(View.GONE);

                            UserInfo userInfo = snapshot.getValue(UserInfo.class);
                            holder.binding.title.setText(model.getTitle());
                            holder.binding.name.setText(name);
                            holder.binding.category.setText(model.getCategory());
                            holder.binding.amount.setText(model.getAmount());
                            holder.binding.startdate.setText(model.getStart_date());
                            holder.binding.enddate.setText(model.getEnd_date());

                        }
                        else {

                            holder.binding.needyextrafields.setVisibility(View.VISIBLE);
                            holder.binding.paynow.setVisibility(View.VISIBLE);
                            UserInfo userInfo = snapshot.getValue(UserInfo.class);
                            holder.binding.name.setText(name);
                            holder.binding.category.setText(model.getCategory());
                            holder.binding.title.setText(model.getTitle());
                            holder.binding.amount.setText(model.getAmount());
                            holder.binding.startdate.setText(model.getStart_date());
                            holder.binding.enddate.setText(model.getEnd_date());
                        }
                    } else {
                        holder.binding.needyextrafields.setVisibility(View.GONE);
                        holder.binding.paynow.setVisibility(View.GONE);
                        UserInfo userInfo = snapshot.getValue(UserInfo.class);
                        holder.binding.name.setText(name);
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.binding.paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onclicklistener.onitemclick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  interface OnClickListnener
    {
        public void onitemclick();

    }
    public class viewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView category,amount,startdate,enddate;
        TextView postdes,paynow;
        ImageView imagepost;
        TableLayout needyextrafields;
        TextView title;

        DonordashboardBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DonordashboardBinding.bind(itemView);

            name = itemView.findViewById(R.id.name);
            postdes = itemView.findViewById(R.id.postdescrp);
            imagepost = itemView.findViewById(R.id.postImg);

            category = itemView.findViewById(R.id.category);
            amount = itemView.findViewById(R.id.amount);
            startdate = itemView.findViewById(R.id.startdate);
            enddate = itemView.findViewById(R.id.enddate);

            title = itemView.findViewById(R.id.destitleofpost);

            needyextrafields=itemView.findViewById(R.id.needyextrafields);
          //  paylayout=itemView.findViewById(R.id.paylayout);
            paynow=itemView.findViewById(R.id.paynow);

        }
    }
}

