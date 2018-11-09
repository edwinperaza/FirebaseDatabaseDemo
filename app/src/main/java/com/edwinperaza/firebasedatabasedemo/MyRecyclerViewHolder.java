package com.edwinperaza.firebasedatabasedemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView name, lastName, email;

    public MyRecyclerViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.tv_name);
        lastName = itemView.findViewById(R.id.tv_last_name);
        email = itemView.findViewById(R.id.tv_email);
    }
}
