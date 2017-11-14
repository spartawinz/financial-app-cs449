package com.easyfin.travis.easyfin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class HomeActivity extends Fragment {
    private Context context;
    private static HomeActivity activity;
    public static HomeActivity getInstance()
    {
        if (activity == null)
        {
            activity = new HomeActivity();
        }
        return activity;
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
        GraphView linegraph = (GraphView) getView().findViewById(R.id.line_graph);
        PieChart piegraph = (PieChart) getView().findViewById(R.id.pie_graph);

        Switch graphSwitch = (Switch) getView().findViewById(R.id.graph_switch);
        graphSwitch.setChecked(false);
        checkSwitchState();

        double earnings = preferenceHandler.getInstance().getMonthlyEarning(context);
        if(earnings!=0.0)
        {
            refreshEditText(earnings);
            refreshGraphs();
        }
        // listeners
        Button earningsBttn = (Button) getView().findViewById(R.id.submit_monthly_earnings_button);
        earningsBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText amount = (EditText) getView().findViewById(R.id.earning_amount_edit_text);
                if(checkDouble(amount.getText().toString()))
                {
                    preferenceHandler.getInstance().addMonthlyEarning(context,amount.getText().toString());
                    populateLineGraph((GraphView) getView().findViewById(R.id.line_graph));

                    populatePieGraph((PieChart) getView().findViewById(R.id.pie_graph)
                            ,preferenceHandler.getInstance().getMonthlyEarning(context));
                }
            }
        });
        graphSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSwitchState();
            }
        });

        if(earnings !=0.0)
        {
            populateLineGraph(linegraph);
            populatePieGraph(piegraph,earnings);
        }
    }
    // checks to make sure the string input can be parsed to double
    private boolean checkDouble(String input)
    {
        try
        {
            double amount = Double.parseDouble(input);
            if(amount == 0.0)
            {
                System.out.println("input is zero");
                return false;
            }
            return true;
        }
        catch(Exception e)
        {
            System.out.println("cannot parse double from edittext");
            return false;
        }
    }
    //shows the graph based on the state of the switch
    private void checkSwitchState()
    {
        Switch graphSwitch = (Switch) getView().findViewById(R.id.graph_switch);
        PieChart pieChart = (PieChart) getView().findViewById(R.id.pie_graph);
        GraphView linegraph = (GraphView) getView().findViewById(R.id.line_graph);
        //disables switch till changes are made
        graphSwitch.setEnabled(false);
        if(graphSwitch.isChecked())
        {
            linegraph.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.INVISIBLE);
        }
        else
        {
            linegraph.setVisibility(View.INVISIBLE);
            pieChart.setVisibility(View.VISIBLE);
        }
        //reenables the switch since it is done processing
        graphSwitch.setEnabled(true);
    }

    // updates the EditText with earnings
    private void refreshEditText(double earnings)
    {
        EditText mEarningsEditText = (EditText) getView().findViewById(R.id.earning_amount_edit_text);
        mEarningsEditText.setText(String.valueOf(earnings));
    }
    // updates the graphs with new data
    private void refreshGraphs()
    {
        GraphView linegraph = (GraphView) getView().findViewById(R.id.line_graph);
        PieChart pieChart = (PieChart) getView().findViewById(R.id.pie_graph);
        populateLineGraph(linegraph);
        populatePieGraph(pieChart,preferenceHandler.getInstance().getMonthlyEarning(context));
    }

    //populates graphs
    private void populateLineGraph(GraphView linegraph)
    {
        linegraph.addSeries(getEarningsDataPoints());
        linegraph.addSeries(getBillsDataPoints());
        linegraph.setScaleX(1F);
        linegraph.getViewport().setMaxY(12);
        linegraph.getViewport().setXAxisBoundsManual(true);
        linegraph.setTitleColor(getResources().getColor(R.color.colorAccent));
        linegraph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.white));
        linegraph.getGridLabelRenderer().setHorizontalLabelsColor(getResources().getColor(R.color.white));
        linegraph.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.white));
        linegraph.setBackgroundColor(getResources().getColor(R.color.colorWindowBackground));
        linegraph.getGridLabelRenderer().reloadStyles();
        linegraph.invalidate();
    }
    private void populatePieGraph(PieChart piegraph,double earnings)
    {
        PieData data = getPieData(earnings);
        piegraph.setData(data);
        piegraph.setUsePercentValues(false);
        piegraph.setCenterTextColor(getResources().getColor(R.color.colorAccent));
        piegraph.setBackgroundColor(getResources().getColor(R.color.colorWindowBackground));
        piegraph.setEntryLabelColor(getResources().getColor(R.color.colorPrimaryDark));
        piegraph.setEntryLabelTextSize(20);
        piegraph.getDescription().setTextSize(15);
        piegraph.getDescription().setTextColor(getResources().getColor(R.color.white));
        piegraph.getDescription().setText("Spending");
        piegraph.getLegend().setTextSize(15);
        piegraph.getLegend().setTextColor(getResources().getColor(R.color.white));
        piegraph.invalidate();
    }

    // returns a series from 1 to 12 where the earnings is the y
    private LineGraphSeries<DataPoint> getEarningsDataPoints()
    {
        double earnings = preferenceHandler.getInstance().getMonthlyEarning(context);
        LineGraphSeries<DataPoint> points = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(1,earnings),
                new DataPoint(2,earnings),
                new DataPoint(3,earnings),
                new DataPoint(4,earnings),
                new DataPoint(5,earnings),
                new DataPoint(6,earnings),
                new DataPoint(7,earnings),
                new DataPoint(8,earnings),
                new DataPoint(9,earnings),
                new DataPoint(10,earnings),
                new DataPoint(11,earnings),
                new DataPoint(12,earnings)
        });
        points.setColor(getResources().getColor(R.color.colorPrimaryDark));
        points.setThickness(10);
        return points;
    }
    //returns list of double arrays containing the month of the bill and amount
    private List<double[]> getBillMonths()
    {
        List<double[]> bills = preferenceHandler.getInstance().getBillsThisYear(context);
        return bills;
    }
    //returns series that has all the amounts added to the correct months
    private LineGraphSeries<DataPoint> getBillsDataPoints()
    {
        List<double[]> bills = getBillMonths();
        List<double[]> formattedBills = new LinkedList<>();
        for(int i = 1; i < 13; i++)
        {
            double amount = 0.0;
            for(double[] bill:bills)
            {
                if(i == bill[0])
                {
                    amount+= bill[1];
                }
            }
            double[] tmp = {i,amount};
            formattedBills.add(tmp);
        }
        // now that we have formatted bills we must put them manually into a series this can be a bit hairy
        LineGraphSeries<DataPoint> points = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(formattedBills.get(0)[0],formattedBills.get(0)[1]),
                new DataPoint(formattedBills.get(1)[0],formattedBills.get(1)[1]),
                new DataPoint(formattedBills.get(2)[0],formattedBills.get(2)[1]),
                new DataPoint(formattedBills.get(3)[0],formattedBills.get(3)[1]),
                new DataPoint(formattedBills.get(4)[0],formattedBills.get(4)[1]),
                new DataPoint(formattedBills.get(5)[0],formattedBills.get(5)[1]),
                new DataPoint(formattedBills.get(6)[0],formattedBills.get(6)[1]),
                new DataPoint(formattedBills.get(7)[0],formattedBills.get(7)[1]),
                new DataPoint(formattedBills.get(8)[0],formattedBills.get(8)[1]),
                new DataPoint(formattedBills.get(9)[0],formattedBills.get(9)[1]),
                new DataPoint(formattedBills.get(10)[0],formattedBills.get(10)[1]),
                new DataPoint(formattedBills.get(11)[0],formattedBills.get(11)[1])
        });
        points.setColor(getResources().getColor(R.color.colorAccent));
        points.setThickness(10);
        return points;
    }
    //returns data into a PieData object
    private PieData getPieData(double earnings)
    {
        int thisMonth = Calendar.getInstance().get(Calendar.MONTH)+1;// since calendar indexes month starting at 0
        float total =Float.parseFloat(String.valueOf(earnings));
        float billsAmount = 0;
        List<double[]> bills = getBillMonths();
        List<double[]> billsThisMonth = new LinkedList<>();
        for(double[] bill:bills)
        {
            if(thisMonth == (int)bill[0])
            {
                billsThisMonth.add(bill);
            }
        }
        for(double[] bill:billsThisMonth)
        {
            billsAmount += bill[1];
        }

        List<PieEntry> entries = new LinkedList<>();
        entries.add(new PieEntry(billsAmount,"bills amount"));

        if(total > billsAmount)
        {
            entries.add(new PieEntry(total-billsAmount,"Free Funds"));
        }

        PieDataSet set = new PieDataSet(entries,"Bills vs. Earnings");
        set.setColors(getResources().getColor(R.color.RedPie),getResources().getColor(R.color.BluePie));
        PieData data = new PieData(set);
        data.setValueTextSize(20);
        return data;
    }
}
