package com.example.cmpt276_2021_7_manganese;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * This TimeoutNotification class is used to make channels for notification.
 * It creates the notification for when the timer is done.
 * @author Rio Samson
 */
public class TimeoutNotification extends Application {
    public static final String CHANNEL_1_ID = "TimerDone";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotification();
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel timerDone = new NotificationChannel(
                    CHANNEL_1_ID, getString(R.string.timeout_timer_done), NotificationManager.IMPORTANCE_HIGH
            );
            timerDone.setDescription(getString(R.string.timer_done_channel));
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(timerDone);
        }
    }
}
