package com.example.finalyearproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.finalyearproject.Model.Transactions;
import com.example.finalyearproject.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    ArrayList<Transactions> list;
    Context context;


    public TransactionAdapter(ArrayList<Transactions> list, Context context) {
        this.context = context;
        this.list=list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_order_seler, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
            final Transactions transactions = list.get(i);
            viewHolder.amount.setText("Rs. "+transactions.getAmount());
            viewHolder.date.setText(""+transactions.getDate());
            viewHolder.sentby.setText(""+transactions.getSender_name());
            viewHolder.reciever.setText(""+transactions.getReciever_name());


        }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amount, date, sentby,reciever;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            sentby = itemView.findViewById(R.id.sentby);
            reciever = itemView.findViewById(R.id.reciever);

        }
    }
}
