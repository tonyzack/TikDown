package com.nadhholy.tikdownloader.video.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.nadhholy.tikdownloader.video.R;


public class AdsUtils {

    private static final String TAG = "GGGG";
    private static boolean isLoading;
    private static InterstitialAd mInterstitialAd;

    private static boolean loading;
    public static void loadAdd(final Context context){
        if (context == null) return;
//        if (ConfigPref.getInstance(context.getApplicationContext())
//                .getConfig().isPremium()) return;

        // 60% de chance
//        if (Math.random() > .5) return;

        if (mInterstitialAd != null)
            return;

        if (!loading) {

            AdRequest adRequest = new AdRequest.Builder().build();

            loading = true;
            InterstitialAd.load(context, context.getString(R.string.INTERSTIEL), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;
                    Log.i(TAG, "onAdLoaded");
                    loading = false;

                    interstitialAd.setFullScreenContentCallback(
                            new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    mInterstitialAd = null;
                                    loadAdd(context);
                                    Log.d(TAG, "The ad was dismissed.");
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(AdError adError) {
                                    // Called when fullscreen content failed to show.
                                    // Make sure to set your reference to null so you don't
                                    // show it a second time.
//                                    lastShow = System.currentTimeMillis();
                                    Log.d(TAG, "The ad failed to show. "+adError.getMessage());
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    // Called when fullscreen content is shown.
                                    Log.d("TAG", "The ad was shown.");
                                }
                            });
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    Log.i(TAG, loadAdError.getMessage());
                    mInterstitialAd = null;
                    loading = false;
                }
            });
        }

    }

    public static void showAdd(Activity context){

        if (context == null) return;

        if (mInterstitialAd == null){ loadAdd(context); return; }

        mInterstitialAd.show(context);

        loadAdd(context);
    }

    public static void showAddOnly(Activity context){

        if (context == null) return;

        if (mInterstitialAd == null){ loadAdd(context); return; }

        mInterstitialAd.show(context);
    }

}

