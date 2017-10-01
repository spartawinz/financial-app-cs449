package com.easyfin.travis.easyfin;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class StocksActivity extends Fragment {
    // just instantiates the instance of preferenceHandler if it hasn't already been made.
    public preferenceHandler handler = preferenceHandler.getInstance();
    // converts the names of the inputted to link addresses that are readable by the URLconnection.
    private List<String> getAddresses(List<String> strs)
    {
        List<String> addresses = new LinkedList<>();
        for(String str:strs)
        {
            addresses.add("https://min-api.cryptocompare.com/data/price?fsym="+String.valueOf(str.toUpperCase())+"&tsyms=USD,BTC");
        }
        return addresses;
    }
    //initial coins when you first start the app.
    private List<String> initCoins()
    {
        List<String> temp = new LinkedList<>();
        temp.add("XMR");
        temp.add("ZEC");
        temp.add("ETH");
        temp.add("DASH");
        temp.add("LTC");
        preferenceHandler.getInstance().addCoinIds(getActivity().getApplicationContext(),temp);
        return temp;
    }
    // instantiates the fragment
    public static StocksActivity newInstance() {
        StocksActivity fragment = new StocksActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AsyncTask<List<String>,Void,List<String>> stocks = new stockURL().execute(getAddresses(initCoins()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_stocks, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        // listener for the search button
        Button searchBttn = (Button) getView().findViewById(R.id.stock_search_button);
        searchBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText text = (EditText) getView().findViewById(R.id.stock_search_EditText);
                clearTables();
                if(!(text.getText().toString() == String.valueOf("")))
                {
                    List<String> names = processText(new StringBuffer(text.getText().toString()));
                    preferenceHandler.getInstance().addCoinIds(getActivity().getApplicationContext(),names);
                    AsyncTask<List<String>,Void,List<String>> stocksEdited = new stockURL().execute(getAddresses(names));
                }
            }
        });
        Button refreshButton = (Button) getView().findViewById(R.id.refresh_spinner_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshSpinner();
            }
        });

        refreshSpinner();
        Spinner stockSpinner = (Spinner) getView().findViewById(R.id.stock_spinner);

        //sets up and adds a onselectedlistener to the spinner this will return the values of the favorite.
        stockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearTables();
                List<String> ids = preferenceHandler.getInstance().getFavoritesId(getActivity().getApplicationContext());
                int count = 0;
                Iterator<String> idItr = ids.iterator();
                List<String> name = new LinkedList<String>();
                List<String> nameSet = preferenceHandler.getInstance().getFavoritesName(getActivity().getApplicationContext());
                Iterator<String> nameItr = nameSet.iterator();
                // finds the position of the favoritesid and returns the values for it
                while(count != position && idItr.hasNext())
                {
                    if(count == position)
                    {
                        List<String> temp = new LinkedList<>();
                        temp.add(idItr.next());
                        name.add(nameItr.next());
                        preferenceHandler.getInstance().addCoinIds(getActivity().getApplicationContext(),name);
                        AsyncTask<List<String>,Void,List<String>> stockFavorite = new stockURL().execute(temp);
                        return;
                    }
                    count++;
                    idItr.next();
                    nameItr.next();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
    // popups the add_stock_favorite activity
    protected void showPopup()
    {
        startActivity(new Intent(getActivity(),add_stock_favorite.class));
    }
    // allows for the connection to the database and returns values
    private class stockURL extends AsyncTask<List<String>,Void,List<String>>
    {
        @Override
        protected List<String> doInBackground(List<String>...lst) {
            List<String> txts = new LinkedList<>();
            List<String> strings = lst[0];
            for(int i = 0; i < strings.size(); i++)
            {
                List<String> tmp = processURL(strings.get(i));
                for(int j = 0; j < tmp.size(); j++)
                {
                    txts.add(tmp.get(j));
                }
            }
            if (!txts.isEmpty())
            {
                return txts;
            }
            else
            {
                return new LinkedList<String>();
            }

        }

        @Override
        protected void onPostExecute(List<String> txts)
        {
            Iterator<String> itr = txts.listIterator();
            int count = 1;
            Resources r = getView().getResources();
            String id_name;
            List<String> nameSet = preferenceHandler.getInstance().getCoinIds(getActivity().getApplicationContext());
            Iterator<String> names = nameSet.iterator();
            while(itr.hasNext() && count <=5)
            {
                id_name = "tablename"+String.valueOf(count);
                TextView view = (TextView) getView().findViewById(r.getIdentifier(id_name,"id",getActivity().getPackageName()));
                view.setText(names.next());
                id_name ="tableusd"+String.valueOf(count);
                view = (TextView) getView().findViewById(r.getIdentifier(id_name,"id",getActivity().getPackageName()));
                view.setText(itr.next());
                id_name ="tablebtc"+String.valueOf(count);
                view = (TextView) getView().findViewById(r.getIdentifier(id_name,"id",getActivity().getPackageName()));
                view.setText(itr.next());
                count++;
            }
        }

    }
    private List<String> processData(BufferedReader reader) throws IOException
    {
        List<String> lst = new LinkedList<>();
        String item = "";
        try{
            StringBuffer buffer = new StringBuffer();
            buffer.append(reader.readLine());
            //takes the buffer and starts reading it character by character and divides it up into chunks
            for(int i = 0; i<buffer.length();i++)
            {
                switch(buffer.charAt(i))
                {
                    case '{':
                        break;
                    case '"':
                        break;
                    case ',':
                        lst.add(item);
                        item = "";
                        break;
                    case '}':
                        lst.add(item);
                        item="";
                        break;
                    case ':':
                        break;
                    default:
                        if(Character.isDigit(buffer.charAt(i)) || buffer.charAt(i) == '.')
                        {
                            item+=buffer.charAt(i);
                        }
                }
            }
        }
        catch(Exception e)
        {

        }
        return lst;
    }
    // processes the URL and returns the data in a buffer
    private List<String> processURL(String urlhttp) {
        try {
            URL url = new URL(urlhttp);
            HttpURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "EasyFin");
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                InputStream response = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
                List<String> lst = processData(reader);
                return lst;
            }
        }
        catch (Exception e) {
            return new LinkedList<String>();
        }
        return new LinkedList<String>();
    }
    private List<String> processText(StringBuffer buffer)
    {
        List<String> lst = new LinkedList<>();
        String item = "";
        try{
            //takes the buffer and starts reading it character by character and divides it up into chunks
            for(int i = 0; i<buffer.length();i++)
            {
                switch(buffer.charAt(i))
                {
                    case ',':
                        lst.add(item);
                        item = "";
                        break;
                    default:
                        if(Character.isLetter(buffer.charAt(i)))
                        {
                            item+=buffer.charAt(i);
                        }
                }
            }
        }
        catch(Exception e)
        {

        }
        if(item != "")
        {
            lst.add(item);
        }
        return lst;
    }
    // refreshes the spinners data
    public void refreshSpinner()
    {
        ArrayList<String> entries = new ArrayList<>();
        List<String> strs = preferenceHandler.getInstance().getFavoritesName(getActivity().getApplicationContext());
        for(String str:strs)
        {
            entries.add(str);
        }
        Spinner stockSpinner = (Spinner) getView().findViewById(R.id.stock_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,entries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.notifyDataSetChanged();
        stockSpinner.setAdapter(adapter);
    }
    //clears tables of values
    private void clearTables()
    {
        int count = 1;
        Resources r = getView().getResources();
        String id_name;
        String empty = "";
        while( count <=5)
        {
            id_name = "tablename"+String.valueOf(count);
            TextView view = (TextView) getView().findViewById(r.getIdentifier(id_name,"id",getActivity().getPackageName()));
            if(view != null)
                view.setText(empty);
            id_name ="tableusd"+String.valueOf(count);
            view = (TextView) getView().findViewById(r.getIdentifier(id_name,"id",getActivity().getPackageName()));
            if(view != null)
                view.setText(empty);
            id_name ="tablebtc"+String.valueOf(count);
            view = (TextView) getView().findViewById(r.getIdentifier(id_name,"id",getActivity().getPackageName()));
            if (view != null)
                view.setText(empty);
            count++;
        }
    }

}
