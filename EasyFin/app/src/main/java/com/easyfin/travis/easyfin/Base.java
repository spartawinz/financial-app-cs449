package com.easyfin.travis.easyfin;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.List;

public class Base extends AppCompatActivity {
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                selectedFragment = HomeActivity.newInstance();
                                break;
                            case R.id.navigation_bills:
                                selectedFragment = BillsActivity.getInstance();
                                break;
                            case R.id.navigation_stocks:
                                selectedFragment = StocksActivity.newInstance();
                                break;
                            default:
                                new Exception("Invalid Button.");
                                return false;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_container, selectedFragment);
                        transaction.commit();
                        context = getApplicationContext();
                        return true;
                    }
                });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, HomeActivity.newInstance());
        transaction.commit();

    }

    /*@Override
    protected void onStart() {
        super.onStart();
        // runs notifications in the background while running the app
        notificationBackground background = new notificationBackground();
        background.execute();
    }*/

   /* private class notificationBackground extends AsyncTask<Void,Void,String>
    {
        // checks to see if there is a new notification to deliver after certain time
        @Override
        protected String doInBackground(Void... params) {
            try {
                do {
                    Thread.sleep(100);
                    if (BillsActivity.getInstance().checkBillDue()) {
                        notifications n = new notifications(context);
                        n.sendNotification(preferenceHandler.getInstance().getBillsDue(context));
                    } else {
                        Thread.sleep(120000);
                    }
                } while (true);
            }
            catch(InterruptedException e)
            {
                System.out.println("Thread interrupted.");
            }
            return "";
        }
    }*/
}

