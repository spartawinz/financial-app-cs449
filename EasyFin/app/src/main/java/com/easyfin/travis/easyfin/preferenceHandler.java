package com.easyfin.travis.easyfin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Travis on 9/30/2017.
 */

public class preferenceHandler {
    // The purpose of this class is to allow for the storage of data using a single sharedpreferences across activities
    public static preferenceHandler instance = null;
    protected preferenceHandler()
    {

    }
    public static preferenceHandler getInstance()
    {
        if(instance == null)
            instance = new preferenceHandler();
        return instance;
    }
    public void addFavorite(Context context, String id, String name)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> temp = prefs.getStringSet("favorite_id", new HashSet<String>());
        temp.add(id);
        editor.putStringSet("favorite_id",temp);
        editor.commit();
        temp = prefs.getStringSet("favorite_name",new HashSet<String>());
        temp.add(name);
        editor.putStringSet("favorite_name",temp);
        editor.commit();
    }
    public Set<String> getFavoritesId(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getStringSet("favorite_id",new HashSet<String>());
    }
    public Set<String> getFavoritesName(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getStringSet("favorite_name",new HashSet<String>());
    }
}
