package com.vincler.jf.projet6.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.Search;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private ArrayList<Search> searchList;
    private SearchAdapterClick click;
    public SearchAdapter(ArrayList<Search> searchList, SearchAdapterClick click) {
        this.searchList = searchList;
        this.click = click;
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
        textView.setText(searchList.get(position).getName());
        View item_search = holder.itemView.findViewById(R.id.item_search);
        item_search.setOnClickListener(v -> {
            click.onClick(searchList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }


}
