package com.mobdeve.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SavedPlacesAdapter extends RecyclerView.Adapter<SavedPlacesAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList id, label;

    public static final int ADD_EDIT_REQUEST_CODE = 1;

    SavedPlacesAdapter(Context context, Activity activity, ArrayList id, ArrayList label){
        this.context = context;
        this.id = id;
        this.label = label;
        this.activity = activity;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.savedplaces_rows, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        //    holder.id_txt.setText(String.valueOf(id.get(position)));
        holder.label_txt.setText(String.valueOf(label.get(position)));

        // Set click listener for the "More" button
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open a menu when the button is clicked
                PopupMenu popupMenu = new PopupMenu(context, holder.btnMore);

                // Use the context of the holder to get the correct resources
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.actions, popupMenu.getMenu());


                // Set menu item click listener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        int option = item.getItemId();
                        if (option == R.id.editOption) {
                            // Handle delete action

                            Intent intent = new Intent(context, EditSavedPlaceActivity.class);
                            intent.putExtra("id", String.valueOf(id.get(position)));
                            intent.putExtra("label", String.valueOf(label.get(position)));

                            activity.startActivityForResult(intent, ADD_EDIT_REQUEST_CODE);

                            return true;
                        }

                        else {
                            return false;
                        }
                    }
                });

                popupMenu.show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id_txt, label_txt;

        ImageView btnMore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            label_txt = itemView.findViewById(R.id.tvSavedPlace);
            btnMore = itemView.findViewById(R.id.btnMore);

        }
    }
}
