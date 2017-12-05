package com.appodeal.mraidtestapp;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class TestsAdapter extends RecyclerView.Adapter<TestsAdapter.ViewHolder> {
    interface ClickListener {
        void onItemClick(String name);
    }
    private List<String> testsSet;
    private ClickListener clickListener;

    TestsAdapter(ClickListener clickListener) {
        this.testsSet = new ArrayList<>();
        this.clickListener = clickListener;
    }

    void fill(List<String> testsSet) {
        this.testsSet = testsSet;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.name.setText(testsSet.get(position));
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(testsSet.get(holder.getAdapterPosition()));
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text);
        }
    }

    @Override
    public int getItemCount() {
        return testsSet.size();
    }
}
