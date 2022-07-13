package com.nadhholy.tikdownloader.video.activities;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


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
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.nadhholy.tikdownloader.video.MyIntents;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.data.Repository;
import com.nadhholy.tikdownloader.video.models.Aweme;
import com.nadhholy.tikdownloader.video.utils.AdsUtils;
import com.nadhholy.tikdownloader.video.utils.Utils;

import java.io.File;

public class MusicActivity extends AppCompatActivity {

    private SimpleExoPlayer player;
    private PlayerView playerView;

    private String path;

    private NativeAd nativeAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music);

        playerView = findViewById(R.id.player_view);


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


        //        SimpleExoPlayer player = new SimpleExoPlayer.Builder(context)
//                .build();
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        initializePlayer(path);

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

//        AdsMng.showAdd(this);
        loadAd();
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



    private void loadAd(){

        AdLoader adLoader = new AdLoader.Builder(this, getString(R.string.NATIVE_2))
                .forNativeAd(ads -> {

                    if (ads == null) {
                        return;
                    }

                    nativeAd = ads;

                    FrameLayout frameLayout =
                            findViewById(R.id.native_ad_container);

                    displayNativeAd(frameLayout, nativeAd);

                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .build())
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void displayNativeAd(ViewGroup parent, NativeAd ad) {

        parent.setVisibility(View.VISIBLE);

        // Inflate a layout and add it to the parent ViewGroup.
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        NativeAdView adView = (NativeAdView) inflater
                .inflate(R.layout.ad_layout, null);


        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_icon));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));


        ((TextView)adView.getHeadlineView()).setText(ad.getHeadline());

        if (ad.getHeadline() == null){
            adView.getHeadlineView().setVisibility(View.GONE);
        } else {
            adView.getHeadlineView().setVisibility(View.VISIBLE);
            ((TextView)adView.getHeadlineView()).setText(ad.getHeadline());
        }

        if (ad.getCallToAction() == null){
            adView.getCallToActionView().setVisibility(View.GONE);
        } else {
            ((TextView)adView.getCallToActionView()).setText(ad.getCallToAction());
        }

        if (ad.getIcon() != null){
            ((ImageView)adView.getIconView()).setImageDrawable(ad.getIcon().getDrawable());
        }

        if (ad.getBody() == null){
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            ((TextView)adView.getBodyView()).setText(ad.getBody());
        }

        if (ad.getStore() == null){
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            ((TextView)adView.getStoreView()).setText(ad.getStore());
        }

        if (ad.getPrice() == null){
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            ((TextView)adView.getPriceView()).setText(ad.getPrice());
        }

        if (ad.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(ad.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (ad.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else if (ad.getAdvertiser() != null && ad.getStarRating() == null){
            ((TextView) adView.getAdvertiserView()).setText(ad.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        MediaView mediaView = (MediaView) adView.findViewById(R.id.ad_media);
        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
        adView.setMediaView(mediaView);


        adView.setNativeAd(ad);
        parent.removeAllViews();
        parent.addView(adView);
    }


    @Override
    protected void onPause() {
        player.setPlayWhenReady(false);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopPlayer();
        AdsUtils.showAddOnly(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        AdsMng.showAdd(this);
        super.onBackPressed();
    }

}
