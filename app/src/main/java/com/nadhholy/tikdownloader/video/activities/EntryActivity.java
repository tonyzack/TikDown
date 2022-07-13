package com.nadhholy.tikdownloader.video.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.data.Config;
import com.nadhholy.tikdownloader.video.data.PrefConfig;

import java.lang.ref.WeakReference;


public class EntryActivity extends AppCompatActivity {

    private InitAsyncTask initAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        ImageView logo = findViewById(R.id.logo);

        final Animation animation = AnimationUtils.loadAnimation(this,R.anim.hv);
        animation.setRepeatCount(Animation.INFINITE);

        logo.startAnimation(animation);

        initAsyncTask = new InitAsyncTask(this);
        initAsyncTask.execute();
//        new Handler().postDelayed(this::start, 4000);
    }

    private void start(){
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(0,0);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (initAsyncTask != null && !initAsyncTask.isCancelled()) {
            initAsyncTask.cancel(true);
        }
    }


    static class InitAsyncTask extends AsyncTask<Void, Void, String> {
        private final WeakReference<EntryActivity> weakReference;

        InitAsyncTask(EntryActivity stickerPackListActivity) {
            this.weakReference = new WeakReference<>(stickerPackListActivity);
        }

        @Override
        protected final String doInBackground(Void... lists) {
            MobileAds.initialize(weakReference.get().getApplicationContext());
            Config config = PrefConfig.getInstance(weakReference.get().getApplicationContext()).getConfig();
            if (config.getOpenCount() <= 8){
                config.setOpenCount(config.getOpenCount()+1);
                if (config.getOpenCount() == 8){
                    config.setRated(true);
                }
                PrefConfig.getInstance(weakReference.get().getApplicationContext())
                        .saveConfig(config);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String stringListPair) {

            final EntryActivity entryActivity = weakReference.get();
            if (entryActivity != null) {
                entryActivity.start();
            }
        }
    }
}