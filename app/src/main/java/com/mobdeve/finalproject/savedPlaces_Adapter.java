package com.mobdeve.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.mobdeve.finalproject.databinding.SavedplacesRowsBinding;

import java.util.ArrayList;
import java.util.List;

public class savedPlaces_Adapter extends RecyclerView.Adapter<savedPlaces_Adapter.placesViewHolder> {

    private ArrayList<SavedPlaceItem> places;
    private Activity activity;
    private OnItemClickListener listener;


    public savedPlaces_Adapter(ArrayList<SavedPlaceItem> places, Activity activity, OnItemClickListener listener) {
        this.places = places;
        this.activity = activity;
        this.listener = listener;
    }


    @Override
    public placesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SavedplacesRowsBinding rowsBinding = SavedplacesRowsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new placesViewHolder(rowsBinding);
    }

    @Override
    public void onBindViewHolder(placesViewHolder holder, int position) {
        holder.bindPlacesItem(this.places.get(position), position);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void addPlace(SavedPlaceItem place){
        this.places.add(place);
        notifyItemInserted(this.places.size()-1);
    }

    public void removePlace(int position){
        PlacesDatabase placesDatabase = new PlacesDatabase(activity.getApplicationContext());
        SavedPlaceItem savedPlaceItem = this.places.get(position);
        placesDatabase.deletePlace(savedPlaceItem);

        this.places.remove(position);
        notifyItemRemoved(position);
    }

    public void updatePlace(int position, SavedPlaceItem savedPlace){
        this.places.set(position, savedPlace);
        notifyItemChanged(position);
    }

    public interface OnItemClickListener{
        void onItemClick(float latitude, float longitude);
    }

    public class placesViewHolder extends RecyclerView.ViewHolder{
        private SavedplacesRowsBinding itemBinding;
        private int myPosition = -1;
        private SavedPlaceItem item;

        private ImageButton btnMore;

        placesViewHolder(SavedplacesRowsBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;

            btnMore = itemBinding.btnMore;

            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopup();
                }
            });

            View.OnClickListener rowClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    float latitude = places.get(position).getLatitude();
                    float longitude = places.get(position).getLongitude();
                    listener.onItemClick(latitude, longitude);
                }
            };

            itemView.setOnClickListener(rowClickListener);
            itemBinding.tvSavedPlace.setOnClickListener(rowClickListener);



        }

        private void showPopup(){
            PopupMenu popup = new PopupMenu(itemView.getContext(), itemView, Gravity.END);
            popup.inflate(R.menu.actions);

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(menuItem.getItemId() == R.id.editOption){
                        Intent intent = new Intent(activity, AddEditSavedPlaceActivity.class);
                        activity.startActivity(intent);
                        return true;
                    }
                    else if(menuItem.getItemId() == R.id.deleteOption){
                        int position = getAdapterPosition();
                        removePlace(position);
                        return true;
                    }
                    return false;
                }
            });

            popup.show();
        }

        public void bindPlacesItem(SavedPlaceItem place, int position){
            this.myPosition = position;
            this.item = place;

            this.itemBinding.tvSavedPlace.setText(this.item.getPlaceName());
        }

    }
}
