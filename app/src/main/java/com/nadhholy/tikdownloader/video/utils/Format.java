package com.nadhholy.tikdownloader.video.utils;

import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nadhholy.tikdownloader.video.models.error.Error;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Format {

    public static boolean isEllipsed(TextView textView, String text){

        Layout l = textView.getLayout();

        if (l != null) {
            int lines = l.getLineCount();
            if (lines > 0)
                return l.getEllipsisCount(lines - 1) > 0
                        || TextViewCompat.getMaxLines(textView) > 0;
        }

        return false;
    }

    public static String url(String url){

        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://"+url;

        return url;
    }

    public static String badge9(int i){
        return (i >= 9) ? 9+"+" : i+"";
    }

    public static String simplifyNumber(int number){

        if (number < 1000)
            return "" + number;
        int exp = (int) (Math.log(number) / Math.log(1000));
        return String.format("%.1f %c", number / Math.pow(1000, exp), "kMGTPE".charAt(exp-1));
    }


    public static String toAmount(double value){
        String s;

        if (value < 0)
            return "-";

        if (value == 0)
            return "0";

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat formatter = (DecimalFormat)nf;
        formatter.applyPattern("###,###,###,###.00");
        s = formatter.format(value);

        s = s.substring(0,s.indexOf(".")).replaceAll(","," ")
                +s.substring(s.indexOf("."));

        if (s.endsWith(".00"))
            s = s.replace(".00","");

        return s;
    }

    public static String toAmount(String value){

        double d = Double.valueOf(value);

        String s;

        if (d < 0)
            return "-";

        if (d == 0)
            return "0";

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat formatter = (DecimalFormat)nf;
        formatter.applyPattern("###,###,###,###.00");
        s = formatter.format(d);

        s = s.substring(0,s.indexOf(".")).replaceAll(","," ")
                +s.substring(s.indexOf("."),s.length()).replace(".",",");

        if (s.endsWith(",00"))
            s = s.replace(",00","");

        return s;
    }


    public static boolean isValidAmount(String amount){


        if (TextUtils.isEmpty(amount)) return false;
        try {
            return Double.valueOf(amount) > 0;
        } catch (Exception e){
            return false;
        }
        
    }

    public static boolean isValidCardNum(String num){

        if (!TextUtils.isEmpty(num)) {
            num = num.trim().replace(" ","");
            return num.length() <= 16;
        }

        return false;

    }


    public static boolean isValidPhone(String num){

        if (!TextUtils.isEmpty(num))
            return num.length() >= 8 && num.length() <= 18;

        return false;

    }


    public static Error getError(String s){
        Gson gson = new GsonBuilder().create();

        String message = "Une erreur a été rencontrée. Veuillez rééssayer plus tard";

        if (s != null) {
            try {
                Log.e("ERROR_CODE",s);

                Error error = gson.fromJson(s,Error.class);
                if (error == null)
                    return new Error(0,message);
                return error;
            } catch (Exception e) {
                e.printStackTrace();
                return new Error(0,message);
            }
        }
 
        return new Error(0,message);
    }


    public static String oo(int i) {
        return (i >= 10) ? i+"" : 0+""+i;
    }
    public static String time(int h, int m) {
        return oo(h)+":"+oo(m);
    }
}
