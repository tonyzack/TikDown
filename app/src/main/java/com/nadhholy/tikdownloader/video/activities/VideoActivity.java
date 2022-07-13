package com.nadhholy.tikdownloader.video.activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.nadhholy.tikdownloader.video.MyIntents;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.data.Repository;
import com.nadhholy.tikdownloader.video.models.Aweme;
import com.nadhholy.tikdownloader.video.utils.AdsUtils;
import com.nadhholy.tikdownloader.video.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class VideoActivity extends AppCompatActivity {

    private SimpleExoPlayer player;
    private PlayerView playerView;
    private View adPlaceholderLoader;
    private View adPlaceholder;

    private AdView adView;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        playerView = findViewById(R.id.player_view);
        adPlaceholderLoader = findViewById(R.id.ad_placeholder_loading);
        adPlaceholder = findViewById(R.id.ad_placeholder);

        Aweme mfList = (Aweme) getIntent().getSerializableExtra(MyIntents.DATA);

        if (mfList == null) {
            finish();
            return;
        }

        path = mfList.getLocalPath();

        if (!new File(path).exists()){
            Toast.makeText(this, R.string.nf_md, Toast.LENGTH_LONG).show();
            finish();
        }

        findViewById(R.id.back)
                .setOnClickListener(v -> {
                    onBackPressed();
                });

        findViewById(R.id.delete)
                .setOnClickListener(v -> {
                    AlertDialog dialogg = new AlertDialog.Builder(this, R.style.MyDialogTheme)
                            .setMessage(Utils.fromHtml("<font color='#FFFFFF'>"+getString(R.string.delete_video_confirm)+"</font>"))
                            .setPositiveButton(R.string.delete, (dialog, which) -> {

                                Repository.getInstance(getApplicationContext())
                                        .remove(mfList);
                                Utils.deleteFile(this, path);
                                finish();

                            })
                            .setNegativeButton(R.string.dismiss, null)
                            .create();


                    dialogg.setOnShowListener(arg0 -> {
                        dialogg.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
                        dialogg.getButton(AlertDialog.BUTTON_POSITIVE)
                                .setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                    });
                    dialogg.show();
                });

        findViewById(R.id.share)
                .setOnClickListener(v -> {
                    Utils.shareVideo(this, mfList);
                });

        //        SimpleExoPlayer player = new SimpleExoPlayer.Builder(context)
//                .build();
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        initializePlayer(path);

        FrameLayout adContainerView = findViewById(R.id.adContainerView);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.BAN_2));
        adContainerView.addView(adView);
        loadBanner();
    }

    private void initializePlayer(String path) {

        Uri uri = Uri.parse(path);



        ExtractorMediaSource audioSource = new ExtractorMediaSource(
                uri,
                new DefaultDataSourceFactory(this, "MyExoplayer"),
                new DefaultExtractorsFactory(),
                null,
                null
        );


        player.prepare(audioSource);
        playerView.setPlayer(player);
        player.setPlayWhenReady(true);

        player.addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady && playbackState == Player.STATE_READY) {
                    // media actually playing
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else if (playWhenReady) {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        });
    }

    public void stopPlayer(){
        if (player != null) {
            player.stop();
            player.release();
        }
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


    @Override
    protected void onDestroy() {
        stopPlayer();
        AdsUtils.showAddOnly(this);
        if (adView != null){
            adView.destroy();
            adView = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        player.setPlayWhenReady(false);
        super.onPause();
    }

}
