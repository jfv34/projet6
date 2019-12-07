package com.vincler.jf.projet6.ui.restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.User;
import com.vincler.jf.projet6.utils.GetStringUtils;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {

    private List<User> users;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public RestaurantAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurantworkmate, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView photo_iv = holder.itemView.findViewById(R.id.item_restaurantWorkmate_iv);
        TextView name_tv = holder.itemView.findViewById(R.id.item_restaurantWorkmate_tv);
        displayPhoto(photo_iv, position);
        displayText(name_tv, position);
    }

    private void displayText(TextView name_tv, int position) {
        String name = users.get(position).getUsername();
        String firstName = GetStringUtils.getFirstWord(name);
        String text = firstName + " " + context.getString(R.string.isjoining);
        name_tv.setText(text);
    }

    private void displayPhoto(ImageView photo_iv, int position) {
        String photoUrl = users.get(position).getPhotoUserUrl();
        if (!photoUrl.isEmpty()) {
            Glide.with(context).load(photoUrl).dontTransform().into(photo_iv);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}