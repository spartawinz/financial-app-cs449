package com.easyfin.travis.easyfin;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class NotificationBuild {
    private NotificationCompat.Builder builder;
    private NotificationManager NManager;
    private Context context;
    NotificationBuild(Context context)
    {
        this.context = context;
        builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Bill Due!")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE);

        NManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    // builds the notification so that each is personalized with the bill name.
    public void sendNotification(String name, String date)
    {
        int id = preferenceHandler.getInstance().getBillNumber(context);
        long timeTill = getTimeTill(date);
        //long timeTill = testTimeTill();
        Intent intent = new Intent(context, Base.class);
        PendingIntent activity = PendingIntent.getActivity(context,id,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        List<String> billsDue = preferenceHandler.getInstance().getBillsDue(context);
        try {
            builder.setContentIntent(activity);
            builder.setContentText(name + " is due!");
            Notification notification = builder.build();
            if(timeTill >= 0)
            {
                System.out.println("timeTill is negative.");
                throw new Exception("timeTill is negative");
            }
            //NManager.notify(id,builder.build());
            // creates an intent for the broadcast
            Intent nIntent = new Intent(context,NotificationBroadcast.class);
            nIntent.putExtra(NotificationBroadcast.NOTIFICATION_ID,id);
            nIntent.putExtra(NotificationBroadcast.NOTIFICATION,notification);

            PendingIntent pIntent = PendingIntent.getBroadcast(context,id,nIntent,PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager AManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            AManager.set(AlarmManager.ELAPSED_REALTIME,timeTill,pIntent);
        }
        catch(Exception e)
        {
            System.out.println("builder is null or timeTill is negative.");
        }

    }

    private long getTimeTill(String date)
    {
        long milliseconds = 0L;
        Calendar today = GregorianCalendar.getInstance();
        SimpleDateFormat fm = new SimpleDateFormat("MM/dd/yyyy");
        String[] billDueString = date.split("/");
        String[] todayString = fm.format(today.getTime()).split("/");
        //if the date is today we will go ahead and pass it 0 so that way it just shows the notification right after you finish
        if(billDueString[0].equals(todayString[0]) && billDueString[1].equals(todayString[1]) && billDueString[2].equals(todayString[2]))
        {
            return 0L;
        }
        //sets up the date so that it sets to 8 am on due date
        Calendar billDue = GregorianCalendar.getInstance();
        billDue.set(Calendar.MONTH,Integer.parseInt(billDueString[0]));
        billDue.set(Calendar.DAY_OF_MONTH,Integer.parseInt(billDueString[1]));
        billDue.set(Calendar.YEAR,Integer.parseInt(billDueString[2]));
        billDue.set(Calendar.HOUR_OF_DAY,8);
        billDue.set(Calendar.MINUTE,0);
        billDue.set(Calendar.SECOND,0);
        billDue.set(Calendar.MILLISECOND,0);
        billDue.set(Calendar.HOUR,Calendar.AM);
        milliseconds = billDue.getTimeInMillis()-today.getTimeInMillis();

        // checks to make sure we expect a positive value if the due date is after today
        assert(milliseconds < 0);
        return milliseconds;
    }
    //returns 0L so that way as soon as the notification is made it will appear to make sure the code works
    private long testTimeTill()
    {
        return 0L;
    }

}
