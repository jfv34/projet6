package com.vincler.jf.projet6.ui.workmates;

import android.content.Context;
import android.graphics.Typeface;
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
import com.vincler.jf.projet6.utils.GetStringUtils;

import java.util.List;

public class ListWorkmatesAdapter extends RecyclerView.Adapter<ListWorkmatesAdapter.ViewHolder> {

    private List<User> users;
    private Context context;
    private ImageView photo_iv;
    private TextView name_tv;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    ListWorkmatesAdapter(List<User> users, Context context) {

        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workmates, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        name_tv = holder.itemView.findViewById(R.id.item_workmates_tv);
        photo_iv = holder.itemView.findViewById(R.id.item_workmates_photo_iv);
        View item_workmates = holder.itemView.findViewById(R.id.item_workmates);

        displayPhoto(position);
        displayText(position);
        item_workmates.setOnClickListener(v -> {
            String restaurantChoice = users.get(position).getRestaurantChoice();
            if (!restaurantChoice.isEmpty()) {
                restaurantActivityIntent(position, users.get(position).getRestaurantChoice());
            }

        });
    }

    private void displayText(int position) {

        String name = users.get(position).getUsername();
        String firstname = GetStringUtils.getFirstWord(name);
        String nameRestaurant = users.get(position).getRestaurantName();

        String text;
        if (nameRestaurant.isEmpty()) {
            text = getDefaultText(firstname);
        } else {
            text = getText(firstname, nameRestaurant);
            name_tv.setTypeface(name_tv.getTypeface(), Typeface.BOLD);
        }
        name_tv.setText(text);
    }

    private String getText(String firstname, String nameRestaurant) {
        String textBeforeCut =
                firstname
                        + " "
                        + context.getString(R.string.iseatingat)
                        + " "
                        + nameRestaurant;
        return GetStringUtils.getNoCutLastWord(textBeforeCut, 60);
    }

    private String getDefaultText(String firstname) {
        return firstname
                + " "
                + context.getString(R.string.hasntdecidedyet);
    }


    private void displayPhoto(int position) {

        String photo = users.get(position).getPhotoUserUrl();
        if (!photo.isEmpty()) {
            Glide.with(context).
                    load(photo).
                    dontTransform().
                    into(photo_iv);

        }
    }

    private void restaurantActivityIntent(int position, String restaurantChoice) {

        Log.i("tag_restaurantIntent", "click");
        /*Intent intent = new Intent(context.getApplicationContext(), RestaurantActivity.class);
        intent.putExtra("restaurant",restau);

        context.startActivity(intent);*/
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}