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
import com.vincler.jf.projet6.models.Search;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private ArrayList<Search> searchList;
    public SearchAdapter(ArrayList<Search> searchList) {
        this.searchList = searchList;
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
        Log.i("tag_search",searchList.get(position).getPlaceId());
        textView.setText(searchList.get(position).getRestaurantName());
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }
}
