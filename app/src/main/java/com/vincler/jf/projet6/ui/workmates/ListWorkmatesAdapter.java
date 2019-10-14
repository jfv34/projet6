package com.vincler.jf.projet6.ui.workmates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.R;

public class ListWorkmatesAdapter extends RecyclerView.Adapter<ListWorkmatesAdapter.ViewHolder> {

    private String[] name;
    private String[] address;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public ListWorkmatesAdapter(String[] name) {
        this.name = name;
        this.address = address;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workmates, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView name_tv = holder.itemView.findViewById(R.id.item_workmates_tv);
        name_tv.setText(name[position]);
    }

    @Override
    public int getItemCount() {
        return name.length;
    }
}