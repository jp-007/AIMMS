package com.example.myapplication;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardHolder extends RecyclerView.ViewHolder {

    static TextView info,id;
    static Button approveButton,rejectButton;

    public CardHolder(@NonNull View itemView) {
        super(itemView);

        this.info=(TextView)itemView.findViewById(R.id.title);
        this.approveButton=(Button) itemView.findViewById(R.id.approveButton);
        this.rejectButton=(Button) itemView.findViewById(R.id.rejectButton);
        this.id=(TextView) itemView.findViewById(R.id.staff_id);
    }
}
