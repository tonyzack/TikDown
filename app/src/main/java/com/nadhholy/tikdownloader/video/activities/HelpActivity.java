package com.nadhholy.tikdownloader.video.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nadhholy.tikdownloader.video.R;

public class HelpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        findViewById(R.id.back)
                .setOnClickListener(v -> {
                    onBackPressed();
                });

        Glide
                .with(this)
                .load(R.drawable.help_1)
                .into((ImageView) findViewById(R.id.image_1));

        Glide
                .with(this)
                .load(R.drawable.help_2)
                .into((ImageView) findViewById(R.id.image_2));

        Glide
                .with(this)
                .load(R.drawable.help_3)
                .into((ImageView) findViewById(R.id.image_3));
    }

}