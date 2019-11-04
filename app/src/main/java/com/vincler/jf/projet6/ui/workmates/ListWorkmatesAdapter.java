package com.vincler.jf.projet6.ui.workmates;

import android.content.Context;
import android.util.Log;
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
import com.vincler.jf.projet6.ui.main.MainActivity;
import com.vincler.jf.projet6.utils.GetStringUtils;

import java.util.List;

public class ListWorkmatesAdapter extends RecyclerView.Adapter<ListWorkmatesAdapter.ViewHolder> {

    private List<User> users;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    ListWorkmatesAdapter(List<User> users) {
        this.users = users;
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

        ImageView photo_iv = holder.itemView.findViewById(R.id.item_workmates_photo_iv);
        String photo = users.get(position).getPhotoUserUrl();
        if (!photo.isEmpty()) {
            Glide.with(holder.itemView.getContext()).
                    load(photo).
                    dontTransform().
                    into(photo_iv);
            Log.i("tag_photo_listworkmates",photo);
        }

        TextView name_tv = holder.itemView.findViewById(R.id.item_workmates_tv);
        String name = users.get(position).getUsername();
        String firstname = GetStringUtils.getFirstWord(name);
        String nameRestaurant = users.get(position).getRestaurantName();
        String textBeforeCut = firstname + " " + "is eating at" + " " + nameRestaurant;
        String text = GetStringUtils.getNoCutLastWord(textBeforeCut,60);
        name_tv.setText(text);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}