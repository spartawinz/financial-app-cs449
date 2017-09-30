package com.easyfin.travis.easyfin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
                addNewFavorite((String.valueOf(stockID.getText())),String.valueOf(stockName.getText()));
                finish();
            }
        });
    }
    protected void addNewFavorite(String favorite_id, String favorite_name)
    {
        preferenceHandler prefs = preferenceHandler.getInstance();
        prefs.addFavorite(getApplicationContext(),favorite_id,favorite_name);
    }


}
