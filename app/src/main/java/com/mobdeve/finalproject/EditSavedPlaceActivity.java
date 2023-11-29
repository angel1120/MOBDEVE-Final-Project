package com.mobdeve.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditSavedPlaceActivity extends AppCompatActivity {
    EditText labelInput;
    Button save;
    String id, label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_saved_place);

        save = findViewById(R.id.btnSaveEdit);
        labelInput = findViewById(R.id.etLabelEdit);

        id = getIntent().getStringExtra("id");
        label = getIntent().getStringExtra("label");
        labelInput.setText(label);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // refactor to use DatabaseHandler.java instead of DatabaseHandlerKotlin.kt

                /*DatabaseHandler myDB = new DatabaseHandler(EditSavedPlaceActivity.this);
                label = labelInput.getText().toString().trim();
                myDB.updateData(id, label);*/

                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();

            }
        });

    }


}
