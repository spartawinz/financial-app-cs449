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
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BillsActivity extends Fragment {
    private static BillsActivity activity;
    private Context AContext;
    public static BillsActivity getInstance() {
        if (activity == null)
        {
            activity = new BillsActivity();
        }
        return activity;
    }
    private List<TableRow> rows = new LinkedList<>();

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
        AContext = getActivity().getApplicationContext();
        refreshViews();
        try {
            final Button refreshButton = (Button) getView().findViewById(R.id.bills_refresh_button);
            refreshButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshViews();
                }
            });
            final EditText searchText = (EditText) getView().findViewById(R.id.bills_search_field);
            final Button searchButton = (Button) getView().findViewById(R.id.bills_search_button);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchBillName(searchText.getText().toString());
                }
            });
            final Button deleteButton = (Button) getView().findViewById(R.id.bills_delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteSelectedViews();
                }
            });
        }
        catch(Exception e)
        {
            System.out.println("bills_refresh_button doesn't exist or is null.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshViews();
    }

    private void showPopup()
    {
        startActivity(new Intent(getActivity(),add_bill_activity.class));
    }
    private void refreshViews()
    {
        List<String> billData = preferenceHandler.getInstance().getBillData(AContext);
        Iterator<String> itr = billData.iterator();
        rows.clear();
        try {
            TableLayout layout = (TableLayout) getView().findViewById(R.id.tableLayoutBills);
            //clears table before input
            layout.removeAllViews();
            for (int i = 0; i < preferenceHandler.getInstance().getBillNumber(AContext); i++) {
                //sets up the layout so that it wraps_content instead of match_parent
                final TableRow row = new TableRow(getView().getContext());
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
                row.setBackgroundColor(getResources().getColor(R.color.colorWindowBackground));
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(rows.contains(row)) {
                            rows.remove(row);
                            row.setBackgroundColor(getResources().getColor(R.color.colorWindowBackground));
                        }
                        else
                        {
                            row.setBackgroundColor(getResources().getColor(R.color.highlight_tint));
                            rows.add(row);
                        }
                    }
                });
                layout.addView(row);
            }
        }
        catch(Exception e)
        {
            System.out.println("tableLayoutTable is null or doesn't exist.");
        }
    }
    private void searchBillName(String target)
    {
        List<String> billData = preferenceHandler.getInstance().getBillData(AContext);
        Iterator<String> itr = billData.iterator();
        try {
            TableLayout layout = (TableLayout) getView().findViewById(R.id.tableLayoutBills);
            //clears table before input
            layout.removeAllViews();
            for (int i = 0; i < preferenceHandler.getInstance().getBillNumber(AContext); i++) {
                //sets up the layout so that it wraps_content instead of match_parent
                TableRow row = new TableRow(getView().getContext());
                TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 10.f);
                row.setLayoutParams(tableRowParams);
                // grabs the individual pieces of the data
                String name = itr.next();
                String date = itr.next();
                String amount = itr.next();
                if(name.toUpperCase().contains(target.toUpperCase()) || date.contains(target)) {
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
        }
        catch(Exception e)
        {
            System.out.println("tableLayoutTable is null or doesn't exist.");
        }
    }
    //deletes rows that user selected
    private void deleteSelectedViews()
    {
        TableLayout layout = (TableLayout) getView().findViewById(R.id.tableLayoutBills);
        for(TableRow rowdata:rows)
        {
            for(int i = 0; i < layout.getChildCount(); i++)
            {
                if(layout.getChildAt(i).equals(rowdata))
                {
                    preferenceHandler.getInstance().removeSelectedBillData(AContext,i);
                }
            }
        }
        rows.clear();
        refreshViews();
    }
}
