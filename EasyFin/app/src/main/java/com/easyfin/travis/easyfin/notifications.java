package com.easyfin.travis.easyfin;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;

import java.util.List;


public class notifications extends AppCompatActivity {
    private NotificationCompat.Builder builder;
    private NotificationManager manager;
    private Context context;
    notifications(Context context)
    {
        this.context = context;
        builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Bill Due!");

        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    // builds the notification so that each is personalized with the bill name.
    public void sendNotification()
    {
        int id = 001;
        List<String> billsDue = preferenceHandler.getInstance().getBillsDue(context);
        try {
            for (int i = 0; i < billsDue.size(); i++) {
                builder.setContentText(billsDue.get(i) + " is due!");

                manager.notify(id, builder.build());
                id = id + 1;
            }
        }
        catch(Exception e)
        {
            System.out.println("builder is null or manager is null");
        }

    }

}
