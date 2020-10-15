package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ui.depthead.AddStaff;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardHolder> {

    ArrayList<CardContainer> containers;

    Connection con;
    Context context;
    CardContainer cardNo;

    public CardAdapter(Context context, ArrayList<CardContainer> containers) {
        this.containers = containers;
        con=new Connection(context);
        this.context=context;
    }

    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row,null);

        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
        cardNo=containers.get(position);

        CardHolder.info.setText(containers.get(position).getTitle().toString());
        CardHolder.id.setText(containers.get(position).getStaffId().toString());
        CardHolder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                con.approveStaffRequest(CardHolder.id.getText().toString());
                containers.remove(cardNo);
                notifyDataSetChanged();
            }
        });
        CardHolder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                con.rejectStaffRequest(CardHolder.id.getText().toString());
                containers.remove(cardNo);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return containers.size();
    }
}
