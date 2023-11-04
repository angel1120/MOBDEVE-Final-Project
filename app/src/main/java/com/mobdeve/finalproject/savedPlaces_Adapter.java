package com.mobdeve.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class savedPlaces_Adapter extends RecyclerView.Adapter<savedPlaces_Adapter.ViewHolder> {

    private List<String> data;
    private LayoutInflater inflater;
    private AdapterView.OnItemClickListener clickListener;

    public savedPlaces_Adapter(Context context, List<String> data) {
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.savedplaces_rows, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String place = data.get(position);
        holder.tvSavedPlace.setText(place);
        holder.tvSavedPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSavedPlace;

        ViewHolder(View itemView) {
            super(itemView);
            tvSavedPlace = itemView.findViewById(R.id.tvSavedPlace);
        }

    }
}
