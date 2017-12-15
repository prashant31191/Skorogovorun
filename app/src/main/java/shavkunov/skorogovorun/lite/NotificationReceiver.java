package shavkunov.skorogovorun.lite;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Random;

import shavkunov.skorogovorun.lite.controller.MainActivity;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID_NOTIFICATION = "ChannelNotification";
    private static final String LOG_NOTIFICATION = "Notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_NOTIFICATION, "onReceive");
        String contentText = getRandomTextForNotification(context);
        Intent intentToMain = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 15,
                intentToMain, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context,
                        CHANNEL_ID_NOTIFICATION)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.icon_notification)
                        .setColor(context.getResources()
                                .getColor(R.color.colorNotification))
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(contentText)
                        .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                        .setLights(Color.RED, 500, 2000)
                        .setAutoCancel(true);

        Notification notification = new NotificationCompat
                .BigTextStyle(builder).bigText(contentText).build();
        NotificationManagerCompat manager =
                NotificationManagerCompat.from(context);
        manager.notify(1, notification);
    }

    private String getRandomTextForNotification(Context context) {
        if (context != null) {
            String[] texts = context.getResources().getStringArray(R.array.text_for_notification);
            int index = new Random().nextInt(texts.length);
            return texts[index];
        }

        return " ";
    }
}
