package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ui.depthead.AddStaff;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

    ArrayList<TaskContainer> containers;

    Connection con;
    Context context;
    TaskContainer cardNo;


    public TaskAdapter(Context context, ArrayList<TaskContainer> containers) {
        this.containers = containers;
        con=new Connection(context);
        this.context=context;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row1,null);

        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        cardNo=containers.get(position);

        Toast.makeText(context,"ID: "+cardNo.getTitle(),Toast.LENGTH_LONG).show();
        TaskHolder.info.setText(cardNo.getTitle());
        TaskHolder.id.setText(cardNo.getStaffId());
        TaskHolder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,CoordinatorMaintenance.class);
                i.putExtra("id",TaskHolder.info.getText().toString().trim());
                context.startActivity(i);
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
