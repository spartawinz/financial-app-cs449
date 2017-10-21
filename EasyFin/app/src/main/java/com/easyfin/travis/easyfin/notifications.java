package com.easyfin.travis.easyfin;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;

import java.util.List;

public class notifications extends AppCompatActivity {
    private NotificationCompat.Builder builder;
    private NotificationManager manager;
    private int id = 001;
    notifications(Context context)
    {
        builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Bill Due!");

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void sendNotification(String name)
    {
        builder.setContentText(name + "is due!");

        manager.notify(id,builder.build());
    }

}
