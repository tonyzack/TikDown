package com.nadhholy.tikdownloader.video.utils;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.core.content.FileProvider;

import com.nadhholy.tikdownloader.video.R;
import com.nadhholy.tikdownloader.video.models.Aweme;

import java.io.File;
import java.io.IOException;


public class Utils {

    public static String abo = "DELUX";

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (html == null) html = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    public static void hideSystemUI(Window window) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.

        if (Build.VERSION.SDK_INT >= 30) {
            window.setDecorFitsSystemWindows(false);
            WindowInsetsController controller = window.getInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars() | WindowInsets.Type.tappableElement());
                controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public static void deleteFile(Context context, String path) {

        try
        {
            File file = new File(path);
            file.delete();
            if(file.exists()){
                file.getCanonicalFile().delete();
                if(file.exists()){
                    context.deleteFile(file.getName());
                }
            }

//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void shareVideo(Context context, Aweme data) {
        if (context == null) return;
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".provider", new File(data.getLocalPath())));
        share.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_txt));
        context.startActivity(Intent.createChooser(share, context.getString(R.string.share)));
    }

    public static String cleanTextContent(String text)
    {
        // strips off all non-ASCII characters
        text = text.replaceAll("[^\\x00-\\x7F]", "");

        // erases all the ASCII control characters
        text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");

        // removes non-printable characters from Unicode
        text = text.replaceAll("\\p{C}", "");

        text = text.replaceAll("[^ -~]","");

        text = text.replaceAll("[^\\p{ASCII}]", "");

        text = text.replaceAll("\\\\x\\p{XDigit}{2}", "");

        text = text.replaceAll("\\n","");

        text = text.replaceAll("[^\\x20-\\x7e]", "");
        return text.trim();
    }

    public static String time(int sec) {
        if (sec >= 10)
            return " "+sec+" ";
        else return " 0"+sec+" ";
    }

}
