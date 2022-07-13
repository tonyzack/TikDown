package com.nadhholy.tikdownloader.video.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.data.Config;
import com.nadhholy.tikdownloader.video.data.PrefConfig;
import com.nadhholy.tikdownloader.video.dialogs.RateUsDialog;
import com.nadhholy.tikdownloader.video.fragment.MenuFragment;
import com.nadhholy.tikdownloader.video.fragment.SavedsFragment;
import com.nadhholy.tikdownloader.video.fragment.VideoDownloadFragment;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;

    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        navigation.setItemIconTintList(null);

        config = PrefConfig.getInstance(getApplicationContext()).getConfig();

        if (savedInstanceState != null){
            String tag = savedInstanceState.getString("CUR_FRAG");
            replace_fragment_by_tag(tag);
        }else {
            replace_fragment(new VideoDownloadFragment());
        }

        findViewById(R.id.more)
                .setOnClickListener(v -> {
                    navigation.setSelectedItemId(R.id.navigation_menu);
                });
        findViewById(R.id.help)
                .setOnClickListener(v -> {
                    startActivity(new Intent(this, HelpActivity.class));
                });

//        getData();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        switch (item.getItemId()) {
            case R.id.navigation_home:
                replace_fragment(new VideoDownloadFragment());
                return true;
            case R.id.navigation_menu:
                replace_fragment(new MenuFragment());
                return true;
            case R.id.navigation_download:
                replace_fragment(new SavedsFragment());
                return true;
        }
        return false;
    };


    private void replace_fragment(Fragment fragment) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction tr = getSupportFragmentManager().beginTransaction();

        Fragment curFrag = getSupportFragmentManager().getPrimaryNavigationFragment();
        Fragment cacheFrag = getSupportFragmentManager().findFragmentByTag(tag);

        if (curFrag != null) {
            curFrag.onPause();
            tr.hide(curFrag);
        }

        if (cacheFrag == null) {
            tr.add(R.id.main_frame, fragment, tag);
        } else {
            tr.show(cacheFrag);
            if (cacheFrag.getView() != null)
                cacheFrag.onResume();
            fragment = cacheFrag;
        }

        tr.setPrimaryNavigationFragment(fragment);
        tr.commit();
    }


    public void replace_fragment_by_tag(String tag) {

        if (tag == null){
            replace_fragment(new VideoDownloadFragment());
            return;
        }

        if (tag.equals(MenuFragment.class.getSimpleName())) {
            replace_fragment(new MenuFragment());
        } else {
            replace_fragment(new VideoDownloadFragment());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
//        if (exiting){
//            super.onBackPressed();
//            return;
//        }

        if (!config.isRated() && config.getOpenCount() < 3) {
//            exiting = true;
            RateUsDialog rDialog = new RateUsDialog(this, true);
            rDialog.setOnDismissListener(dialogInterface -> {
                config = PrefConfig.getInstance(getApplicationContext()).getConfig();
            });
            rDialog.show();
        } else {
            super.onBackPressed();
        }
    }
}