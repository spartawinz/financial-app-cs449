package com.easyfin.travis.easyfin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeActivity extends Fragment {
    private Context context;
    public static HomeActivity newInstance()
    {
        HomeActivity frag = new HomeActivity();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_home,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity().getApplicationContext();
        // Each part needs to pass the context of the application otherwise if we call it within the function it throws a null pointer exception
        // Checks to see if there is any bill that needs to be alerted at startup
        if(BillsActivity.getInstance().checkBillDue(context))
        {
            notifications billNotify = new notifications(context);
            billNotify.sendNotification();
        }
    }
}
