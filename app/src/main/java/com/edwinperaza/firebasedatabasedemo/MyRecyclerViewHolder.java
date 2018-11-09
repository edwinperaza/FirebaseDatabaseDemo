package com.edwinperaza.firebasedatabasedemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView name, lastName, email;
    IItemClickListener iItemClickListener;

    public void setiItemClickListener(IItemClickListener iItemClickListener) {
        this.iItemClickListener = iItemClickListener;
    }

    public MyRecyclerViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.tv_name);
        lastName = itemView.findViewById(R.id.tv_last_name);
        email = itemView.findViewById(R.id.tv_email);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        iItemClickListener.onClick(v, getAdapterPosition());
    }
}
