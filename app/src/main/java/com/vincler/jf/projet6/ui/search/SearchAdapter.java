package com.vincler.jf.projet6.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.vincler.jf.projet6.R;
import com.vincler.jf.projet6.data.RestaurantsService;
import com.vincler.jf.projet6.models.Search;
import com.vincler.jf.projet6.utils.UnsafeOkHttpClient;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> implements OnMapReadyCallback {
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
        textView.setText(searchList.get(position).getRestaurantName());
        View item_search = holder.itemView.findViewById(R.id.item_search);
        item_search.setOnClickListener(v -> {
            String placeId = searchList.get(position).getPlaceId();
            placeIdSelected(placeId);
        });
    }

    private void placeIdSelected(String placeId) {
        RestaurantsService service;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = UnsafeOkHttpClient.getUnsafeOkHttpClient().addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RestaurantsService.class);

        // result.geometry.location


    }

    ;



    @Override
    public int getItemCount() {
        return searchList.size();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
