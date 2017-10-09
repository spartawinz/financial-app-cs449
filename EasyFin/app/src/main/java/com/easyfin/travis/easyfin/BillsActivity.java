package com.easyfin.travis.easyfin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BillsActivity extends Fragment {
    public static BillsActivity newInstance() {
        BillsActivity fragment = new BillsActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.activity_bills, container, false);
    }
    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.bills_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.action_bar_add_bill:
                showPopup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        refreshViews();
        final Button refreshButton = (Button) getView().findViewById(R.id.bills_refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshViews();
            }
        });
    }
    private void showPopup()
    {
        startActivity(new Intent(getActivity(),add_bill_activity.class));
    }
    private void refreshViews()
    {
        List<TextView> lstViews = new LinkedList<>();
        List<String> billData = preferenceHandler.getInstance().getBillData(getActivity().getApplicationContext());
        Iterator<String> itr = billData.iterator();
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.bills_linear);
        layout.removeAllViews();
        for(int i = 0; i<preferenceHandler.getInstance().getBillNumber(getActivity().getApplicationContext());i++)
        {
            TextView view=new TextView(getActivity());
            view.setText(itr.next()+"      "+itr.next()+"       $"+itr.next());
            view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            lstViews.add(view);
        }
        for(TextView billView:lstViews)
        {
            layout.addView(billView);
        }
    }
}
