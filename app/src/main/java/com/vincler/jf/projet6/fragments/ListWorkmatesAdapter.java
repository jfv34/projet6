package com.vincler.jf.projet6.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.models.Users;

import java.util.List;

public class ListWorkmatesAdapter extends RecyclerView.Adapter {

    private List<Users> users;



    public ListWorkmatesAdapter(List<Users> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Users users1 = new Users("abcdef","ab@hfh.ccom",null);
        users.add(users1);


        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_listworkmates, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((ViewHolder) viewHolder).bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Users users) {
            TextView textView = itemView.findViewById(R.id.item_listworkmates_text_tv);
            ImageView imageView = itemView.findViewById(R.id.item_listworkmates_imageUser_iv);
            textView.setText("this is a text");
        }


    }
}
