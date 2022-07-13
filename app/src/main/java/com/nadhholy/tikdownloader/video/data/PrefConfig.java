package com.nadhholy.tikdownloader.video.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefConfig {

    private static final String PREF_ID = "configs";
    private static final String PREMIUM = "HAS_PREMIUM";
    private static final String RATEd = "RATED";
    private static final String OPEN_COUNT = "RR_COUNT";


    private static PrefConfig prefConfig;
    private static SharedPreferences pref;

    public static PrefConfig getInstance(Context context){

        pref = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);

        if (prefConfig == null)
            prefConfig = new PrefConfig();

        return prefConfig;
    }

    private PrefConfig(){

    }

    public void test(){

        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(RATEd, 1);

        editor.apply();
    }

    public void saveConfig(Config conf){

        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(PREMIUM, conf.isPremium());
        editor.putInt(OPEN_COUNT, conf.getOpenCount());
        editor.putBoolean(RATEd, conf.isRated());

        editor.apply();
    }

    public Config getConfig() {

        Config conf = new Config();

        boolean isPrem = pref.getBoolean(PREMIUM, false);
        try {
            boolean rateRequest = pref.getBoolean(RATEd, false);
            conf.setRated(rateRequest);
        } catch (Exception e){

            int reqest = pref.getInt(RATEd, 0);

            if (reqest == 1)
                conf.setRated(false);
            else if (reqest == 2)
                conf.setRated(true);
            else
                conf.setRated(false);
        }
        int count = pref.getInt(OPEN_COUNT, 0);


        conf.setPremium(isPrem);
        conf.setOpenCount(count);

        return conf;
    }


    public void reset(){
        pref.edit().clear().apply();
    }

}
