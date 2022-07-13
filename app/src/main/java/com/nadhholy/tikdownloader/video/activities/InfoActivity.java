package com.nadhholy.tikdownloader.video.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nadhholy.tikdownloader.video.BuildConfig;
import com.nadhholy.tikdownloader.video.R;


public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Config config = PrefConfig.getInstance(getApplicationContext()).getConfig();

        setContentView(R.layout.activity_info);

        TextView version = findViewById(R.id.version);

        version.setText(BuildConfig.VERSION_NAME);

        findViewById(R.id.licences).setOnClickListener(v -> {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://nadhholy.me/privacy_policy")));
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        AdsMng.showAdd(this);
        super.onBackPressed();
    }

}
