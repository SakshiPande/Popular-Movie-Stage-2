package com.example.popularmoviesstage2.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.popularmoviesstage2.R;
import com.example.popularmoviesstage2.model.TrailersResponse;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private List<TrailersResponse> trailersList;
    private Context mContext;

    public TrailersAdapter(List<TrailersResponse> trailersList, Context context) {
        this.trailersList = trailersList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public TrailersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailers_list_item, parent, false);

        return new TrailersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersViewHolder holder, int position) {
        final TrailersResponse trailer = trailersList.get(position);

        holder.watchTrailers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                try {
                    mContext.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    Log.d("message", ex.getMessage());
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return trailersList.size();
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder {
        private Button watchTrailers;


        TrailersViewHolder(View view) {
            super(view);
            watchTrailers = view.findViewById(R.id.button_watch_trailer);

        }
    }
}