package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private TextView name, description;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name = (TextView) findViewById(R.id.dNameID);
        description = (TextView) findViewById(R.id.dDescriptionID);

        extras = getIntent().getExtras();

        name.setText(extras.getString("name"));
        description.setText(extras.getString("description"));

    }
}