package com.easyfin.travis.easyfin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;
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
    //inserts new monthly earnings into storage
    public void addMonthlyEarning(Context context, String amount)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("monthlyEarning",amount);
        editor.commit();
    }
    //gets monthly earnings from the database
    public double getMonthlyEarning(Context context)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String amount = prefs.getString("monthlyEarning","");
        try {
            return Double.parseDouble(amount);
        }
        catch(Exception e)
        {
            System.out.println("value is either empty or wont parse as double.");
            return 0.0;
        }
    }
    //gets bills that are for this year
    public List<double[]> getBillsThisYear(Context context)
    {
        List<double[]> billsThisYear = new LinkedList<>();
        List<String> bills = getBillData(context);
        Iterator<String> itr = bills.iterator();
        while(itr.hasNext())
        {
            itr.next();
            String[] billDate = itr.next().split("/");
            if(billDate[2].equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))){
                double[] tmp = {Double.parseDouble(billDate[0]),Double.parseDouble(itr.next())};
                billsThisYear.add(tmp);
            }
            else
            {
                itr.next();
            }
        }
        return billsThisYear;
    }
}
