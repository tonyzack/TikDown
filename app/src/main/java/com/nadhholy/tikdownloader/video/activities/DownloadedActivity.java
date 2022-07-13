package com.nadhholy.tikdownloader.video.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.adapters.SavedAdapter;
import com.nadhholy.tikdownloader.video.data.Repository;
import com.nadhholy.tikdownloader.video.dialogs.SearshMusicSheet;
import com.nadhholy.tikdownloader.video.dialogs.VideoMenuSheet;
import com.nadhholy.tikdownloader.video.models.Aweme;
import com.nadhholy.tikdownloader.video.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DownloadedActivity extends AppCompatActivity {

    private View emptyView;
    private View adPlaceholderLoader;
    private View adPlaceholder;
    private RecyclerView rv;
    private AdView adView;
    private SavedAdapter adapter;
    private List<Aweme> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded);

        emptyView = findViewById(R.id.empty_view);
        rv = findViewById(R.id.rv);
        adPlaceholderLoader = findViewById(R.id.ad_placeholder_loading);
        adPlaceholder = findViewById(R.id.ad_placeholder);

//        findViewById(R.id.empty_btn).setOnClickListener(v -> {
//            startActivity(new Intent(getContext(), HelpActivity.class));
//        });

        data = new ArrayList<>();

        adapter = new SavedAdapter(this, data);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        adapter.setOnMenuClickListener(data1 -> {
            VideoMenuSheet sheet = VideoMenuSheet.newInstance(null, s -> {
                switch (s){
                    case VideoMenuSheet.TRILLER:
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data1.getTiktokUrl())));
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case VideoMenuSheet.SHARE:
                        Utils.shareVideo(this, data1);
                        break;
                    case VideoMenuSheet.SEARCH_MUSIC:
                        SearshMusicSheet sh = SearshMusicSheet.newInstance(data1.getMusicTitle());
                        sh.show(getSupportFragmentManager(), sh.getTag());
                        break;
                    case VideoMenuSheet.DELETE:
                        AlertDialog dialogg = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                                .setMessage(Utils.fromHtml("<font color='#FFFFFF'>"+getString(R.string.delete_video_confirm)+"</font>"))
                                .setPositiveButton(R.string.delete, (dialog, which) -> {

                                    Repository.getInstance(getApplicationContext())
                                            .remove(data1);
                                    Utils.deleteFile(this,
                                            data1.getLocalPath());
                                    data.remove(data1);
                                    adapter.notifyDataSetChanged();


                                    if (data.isEmpty()){
                                        emptyView.setVisibility(View.VISIBLE);
                                    }

                                })
                                .setNegativeButton(R.string.dismiss, null)
                                .create();


                        dialogg.setOnShowListener(arg0 -> {
                            dialogg.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                            dialogg.getButton(AlertDialog.BUTTON_POSITIVE)
                                    .setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                        });
                        dialogg.show();
                        break;
                }
            });
            sheet.show(getSupportFragmentManager(), sheet.getTag());
        });


        FrameLayout adContainerView = findViewById(R.id.adContainerView);
        adView = new AdView(getApplicationContext());
        adView.setAdUnitId(getString(R.string.BAN_1));
        adContainerView.addView(adView);
        loadBanner();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadBanner() {

        AdRequest adRequest =
                new AdRequest.Builder()
                        .build();

        AdSize adSize = getAdSize();

        if (adSize == null) return;

        adView.setAdSize(adSize);

        adPlaceholderLoader.startAnimation(AnimationUtils.loadAnimation(this, R.anim.alarm));

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                adPlaceholderLoader.clearAnimation();
                adPlaceholder.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                adPlaceholderLoader.clearAnimation();
                adPlaceholder.setVisibility(View.GONE);
            }

            @Override
            public void onAdClosed() {
                adPlaceholderLoader.clearAnimation();
                adPlaceholder.setVisibility(View.GONE);
            }
        });

        adView.loadAd(adRequest);

    }

    private AdSize getAdSize() {

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void loadData(){
        new Thread(() -> {

            List<Aweme> d = Repository.getInstance(getApplicationContext()).getData();

            if (data == null)
                data = new ArrayList<>();

            data.clear();
            data.addAll(d);

            rv.post(() -> {
                adapter.notifyDataSetChanged();
                if (data.isEmpty())
                    emptyView.setVisibility(View.VISIBLE);
                else
                    emptyView.setVisibility(View.GONE);
            });

        }).start();
    }


    @Override
    public void onDestroy() {
        if (adView != null){
            adView.destroy();
            adView = null;
        }
        super.onDestroy();
    }

}