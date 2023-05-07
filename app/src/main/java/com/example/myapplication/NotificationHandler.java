package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private Context context;
    private NotificationManager manager;
    private static final String CHANNEL_ID = "nail_expert_notification_channel";
    private final int NOTIFICATION_ID = 0;

    public NotificationHandler(Context context){
        this.context = context;
        this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "NailExpert",
                NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(android.R.color.holo_red_dark);
        channel.setDescription("Notification from NailExpert");
        this.manager.createNotificationChannel(channel);
    }

    public void send(String message) {
        Intent intent = new Intent(context, ShopListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0, new Intent[]{intent}, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("NailExpert")
                .setContentText(message)
                .setSmallIcon(R.drawable.notification)
                .setContentIntent(pendingIntent);

        this.manager.notify(NOTIFICATION_ID, builder.build());
    }

    public void cancel() {
        this.manager.cancel(NOTIFICATION_ID);
    }
}
