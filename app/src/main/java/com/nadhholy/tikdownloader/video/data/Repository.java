package com.nadhholy.tikdownloader.video.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.nadhholy.tikdownloader.video.models.Aweme;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Repository {

    private static final String PREF_ID = "repo";
    private static final String DATA = "D";


    private static Repository myShopsCache;

    private static SharedPreferences pref;

    public static Repository getInstance(Context context){

        pref = context.getSharedPreferences(PREF_ID, Context.MODE_PRIVATE);

        if (myShopsCache == null)
            return myShopsCache = new Repository();

        return myShopsCache;
    }

    private Repository(){
    }

    public void save(List<Aweme> data){

        SharedPreferences.Editor editor = pref.edit();

        if (data == null){
            editor.putString(DATA, null);
            return;
        }

        Gson gson = new Gson();
        String json = gson.toJson(data);
        editor.putString(DATA, json);

        editor.apply();
    }

    public void add(Aweme data){

        if (data == null)
            return;

        data.genId();

        List<Aweme> rows = getData();

        rows.add(0, data);

        save(rows);
    }

    public void remove(Aweme data){

        if (data == null)
            return;

        List<Aweme> rows = getData();

        Aweme toDelete = null;
        for (Aweme d : rows){
            if (d.getId() == data.getId()){
                toDelete = d;
                break;
            }
        }

        if (toDelete != null)
            rows.remove(toDelete);


        save(rows);
    }

    public List<Aweme> getData(){

        List<Aweme> data = new LinkedList<>();

        Gson gson = new Gson();
        String json = pref.getString(DATA, null);

        Aweme[] d = gson.fromJson(json, Aweme[].class);

        if (d == null)
            return data;

        data.addAll(Arrays.asList(d));

        return data;
    }


}
