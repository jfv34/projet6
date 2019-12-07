package com.vincler.jf.projet6.ui.search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private ArrayList<String> search;

    public SearchAdapter(ArrayList<String> search) {
        this.search = search;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.item_search_tv);
        Log.i("tag_search",search.get(position));
        textView.setText(search.get(position));
    }

    @Override
    public int getItemCount() {
        return search.size();
    }
}
