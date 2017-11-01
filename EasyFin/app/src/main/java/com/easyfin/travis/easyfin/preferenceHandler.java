package com.easyfin.travis.easyfin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;



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
        String temp = prefs.getString("favorite_id","");
        temp+=","+id;
        editor.putString("favorite_id",temp);
        editor.commit();
        temp = prefs.getString("favorite_name","");
        temp+=","+name;
        editor.putString("favorite_name",temp);
        editor.commit();
    }
    public List<String> getFavoritesId(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String[] favorites = prefs.getString("favorite_id","").split(",");
        List<String> lstFavorites = new LinkedList<>();
        for(String favorite:favorites)
        {
            if(favorite != "")
                lstFavorites.add(favorite);
        }
        return lstFavorites;
    }
    public List<String> getFavoritesName(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String[] favorites = prefs.getString("favorite_name","").split(",");
        List<String> lstFavorites = new LinkedList<>();
        for(String favorite:favorites)
        {
            if(favorite != "")
                lstFavorites.add(favorite);
        }
        return lstFavorites;
    }
    public void addCoinIds(Context context, List<String> names)
    {
        String strNames = "";
        for (int i = 0; i < names.size();i++)
        {
            strNames+=names.get(i);
            if(i != names.size()-1)
                strNames+=",";
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("coinIDs",strNames);
        editor.commit();
    }
    public List<String> getCoinIds(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String[] temp = prefs.getString("coinIDs","").split(",");
        List<String> lstCoins = new LinkedList<>();
        for (String item:temp)
        {
            if(item != "")
                lstCoins.add(item);
        }
        return lstCoins;
    }
    //billsActivity functions
    public void addBillData(String name, String date, double amount, Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        DecimalFormat df = new DecimalFormat("#.00");
        String data = prefs.getString("billData","");
        data+="`"+name+"`"+date+"`"+String.valueOf(df.format(amount));
        editor.putString("billData",data);
        editor.commit();
    }
    // gets the bills as a linked list including the name, date, and amount
    public List<String> getBillData(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        List<String> billData = new LinkedList<>();
        String[] data = prefs.getString("billData","").split("`");
        for (String value:data)
        {
            if(value != String.valueOf(""))
                billData.add(value);
        }
        if (!billData.isEmpty())
            // needed because it puts a blank in front for some reason
            billData.remove(0);
        return billData;
    }
    // returns amount of records of bills
    public int getBillNumber(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String[] billData = prefs.getString("billData","").split("`");
        return billData.length/3;
    }
    // sets the bills due taking in a linked list of strings
    public void setBillsDue(Context context,List<String> billsDue)
    {
        String billsDueString = "";
        for(int i = 0; i < billsDue.size(); i++)
        {
            if(i != billsDue.size()-1)
                billsDueString += billsDue.get(i)+",";
            else
                billsDueString += billsDue.get(i);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("billsDue",billsDueString);
        editor.commit();
    }
    // returns the bills due in a linked list
    public List<String> getBillsDue(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String[] billsDueString = prefs.getString("billsDue","").split(",");
        List<String> billsDue = new LinkedList<>();
        for(String bill:billsDueString)
        {
            billsDue.add(bill);
        }
        return billsDue;
    }
}
