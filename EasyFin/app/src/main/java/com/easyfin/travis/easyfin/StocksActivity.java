package com.easyfin.travis.easyfin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class StocksActivity extends Fragment {
    public static StocksActivity newInstance() {
        StocksActivity fragment = new StocksActivity();
        return fragment;
    }

        @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            AsyncTask<String,Void,String> stocks = new stockURL().execute("https://min-api.cryptocompare.com/data/price?fsym=XMR&tsyms=BTC,USD");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_stocks, container, false);
    }
    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.stocks_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_bar_favorite:
                showPopup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void showPopup()
    {
        startActivity(new Intent(getActivity(),add_stock_favorite.class));
    }
    private class stockURL extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... strings) {
            String txt = "";
            for(int j = 0; j < strings.length; j++) {
                txt += processText(strings[j]);
            }
            if (!txt.equals(""))
            {
                return txt;
            }
            else
            {
                return "NO DATA";
            }

        }

        @Override
        protected void onPostExecute(String txt)
        {
            TextView view = (TextView) getView().findViewById(R.id.testStock);
            view.setText(txt);
        }

    }

    private List<String> processData(BufferedReader reader) throws IOException
    {
        List<String> lst = new LinkedList<>();
        lst.add(reader.readLine());
        return lst;
    }
    private String processText(String urlhttp) {
        String txt = "";
        try {
            URL url = new URL(urlhttp);
            HttpURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "EasyFin");
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                InputStream response = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
                List<String> lst = processData(reader);

                for (int i = 0; i < lst.size(); i++) {
                    txt += lst.get(i);
                }
            }
        }
        catch (Exception e) {
            return "";
        }
        return txt.toString();
    }
}
