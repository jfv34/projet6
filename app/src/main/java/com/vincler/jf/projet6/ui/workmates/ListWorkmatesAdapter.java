package com.vincler.jf.projet6.ui.workmates;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.utils.GetStringUtils;

import java.util.List;

public class ListWorkmatesAdapter extends RecyclerView.Adapter<ListWorkmatesAdapter.ViewHolder> {

    private List<User> users;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }

    public ListWorkmatesAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
Log.i("tag_workmates","oncreatevewholder");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workmates, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TextView name_tv = holder.itemView.findViewById(R.id.item_workmates_tv);

        String name = users.get(position).getUsername();
        String firstname = GetStringUtils.getFirstWord(name);
        String nameRestaurant = users.get(position).getRestaurantName();
        String text = firstname + "  " + "is eating at" + " " + nameRestaurant;

        name_tv.setText(text);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}