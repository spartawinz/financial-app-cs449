package com.easyfin.travis.easyfin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BillsActivity extends Fragment {
    private static BillsActivity activity;
    public static BillsActivity getInstance() {
        if (activity == null)
        {
            activity = new BillsActivity();
        }
        return activity;
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
        try {
            final Button refreshButton = (Button) getView().findViewById(R.id.bills_refresh_button);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshViews();
                }
            });
        }
        catch(Exception e)
        {
            System.out.println("bills_refresh_button doesn't exist or is null.");
        }
    }
    private void showPopup()
    {
        startActivity(new Intent(getActivity(),add_bill_activity.class));
    }
    private void refreshViews()
    {
        List<String> billData = preferenceHandler.getInstance().getBillData(getActivity().getApplicationContext());
        Iterator<String> itr = billData.iterator();
        try {
            TableLayout layout = (TableLayout) getView().findViewById(R.id.tableLayoutBills);
            //clears table before input
            layout.removeAllViews();
            for (int i = 0; i < preferenceHandler.getInstance().getBillNumber(getActivity().getApplicationContext()); i++) {
                //sets up the layout so that it wraps_content instead of match_parent
                TableRow row = new TableRow(getView().getContext());
                TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 10.f);
                row.setLayoutParams(tableRowParams);
                // grabs the individual pieces of the data
                String name = itr.next();
                String date = itr.next();
                String amount = itr.next();
                // adds rows to display the data in the desired format
                TextView view = new TextView(getView().getContext());
                view.setText(name);
                view.setGravity(Gravity.START);
                row.addView(view);
                view = new TextView(getView().getContext());
                view.setText(date);
                row.addView(view);
                view = new TextView(getView().getContext());
                view.setText(amount);
                view.setGravity(Gravity.END);
                row.addView(view);
                layout.addView(row);
            }
        }
        catch(Exception e)
        {
            System.out.println("tableLayoutTable is null or doesn't exist.");
        }
    }
    public boolean checkBillDue(Context context)
    {
        Calendar today = GregorianCalendar.getInstance();
        List<String> bills = preferenceHandler.getInstance().getBillData(context);
        Iterator<String> itr = bills.iterator();
        List<String> names = new LinkedList<>();
        List<String> dates = new LinkedList<>();
        List<String> namesDue = new LinkedList<>();
        try
        {
            while (itr.hasNext())
            {
                names.add(itr.next());
                dates.add(itr.next());
                itr.next();
            }
        }
        catch(Exception e)
        {
            names.clear();
            System.out.println("Invalid data input.");
        }
        if (!names.isEmpty()) {
            for(int i = 0; i < names.size(); i++)
            {
                SimpleDateFormat fm = new SimpleDateFormat("MM/dd/yyyy");
                String[] todaysDate = fm.format(today.getTime()).split("/");
                String[] billDate = dates.get(i).split("/");
                // Checking to make sure that the date splits into 3 parts and not more
                assert (todaysDate.length == 3 && billDate.length == 3);
                if(todaysDate[0].equals(billDate[0]) && todaysDate[1].equals(billDate[1]) && todaysDate[2].equals(billDate[2]) )
                {
                    namesDue.add(names.get(i));
                }
            }
            preferenceHandler.getInstance().setBillsDue(context,namesDue);
            return true;
        }
        else
        {
            return false;
        }
    }
}
