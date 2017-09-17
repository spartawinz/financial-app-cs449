package com.easyfin.travis.easyfin;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class add_stock_favorite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock_favorite);

        Button backBttn = (Button) findViewById(R.id.backBttn);
        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button submitBttn = (Button) findViewById(R.id.submitBttn);
        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView stockID = (TextView)findViewById(R.id.stock_id);
                TextView stockName =(TextView)findViewById(R.id.stock_name);
                addNewFavorite((String)stockID.getText(),(String)stockName.getText());
            }
        });
    }
    protected void addNewFavorite(String favorite_id, String favorite_name)
    {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (prefs.getStringSet("favorites_id",new HashSet<String>()) == new HashSet<String>())
        {
            Set<String> fav = new HashSet<>();
            fav.add(favorite_id);
            editor.putStringSet("favorites_id",fav);
            editor.commit();
            fav = new HashSet<>();
            fav.add(favorite_name);
            editor.putStringSet("favorites_name",fav);
            editor.commit();
        }
        else
        {
            Set<String> fav = prefs.getStringSet("favorites_id",new HashSet<String>());
            fav.add(favorite_id);
            editor.putStringSet("favorites_id",fav);
            editor.commit();
            fav = prefs.getStringSet("favorites_name",new HashSet<String>());
            fav.add(favorite_name);

        }
    }


}
