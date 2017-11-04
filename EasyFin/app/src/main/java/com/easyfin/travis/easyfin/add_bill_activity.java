package com.easyfin.travis.easyfin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class add_bill_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill_activity);

        Button backBttn = (Button) findViewById(R.id.bill_back_button);
        backBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button submitBttn = (Button) findViewById(R.id.bill_submit_button);
        final TextView billName = (TextView) findViewById(R.id.bill_add_name);
        final TextView billDate = (TextView) findViewById(R.id.bill_add_date);
        final TextView billAmount = (TextView) findViewById(R.id.bill_add_amount);
        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!billName.getText().toString().equals("")&&!billDate.getText().toString().equals("")&& !billAmount.getText().toString().equals("") && !billName.getText().toString().contains("`")&&!billDate.getText().toString().contains("`")) {
                    if(billName.getText().toString().length() > 14)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(add_bill_activity.this,R.style.dialogTheme);
                        builder.setTitle("ERROR")
                                .setMessage("Please make the name 14 Characters max.")
                                .setPositiveButton(android.R.string.ok,(new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }))
                                .setCancelable(true);
                        billName.setText("");
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    //adds bill to sharedPreferences and adds the alarm to be triggered at desired time
                    else {
                        addNewBill(billName.getText().toString(), billDate.getText().toString(), Double.parseDouble(billAmount.getText().toString()));
                        addNotificationTimer(billName.getText().toString(),billDate.getText().toString());
                        finish();
                    }
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(add_bill_activity.this,R.style.dialogTheme);
                    builder.setTitle("ERROR")
                            .setMessage("Please fill out all entries and dont use ~")
                            .setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(true);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    billDate.setText("");
                    billName.setText("");
                    billAmount.setText("");
                }
            }
        });
    }
    // puts bill temporarily into the shared preference
    private void addNewBill(String name, String date,double amount)
    {
        preferenceHandler.getInstance().addBillData(name,date,amount,getApplicationContext());
    }
    //creates a timer for the notification even when the app is off
    private void addNotificationTimer(String name, String date)
    {
        NotificationBuild notification = new NotificationBuild(BillsActivity.getInstance().getActivity().getApplicationContext());
        notification.sendNotification(name,date);
    }
}
