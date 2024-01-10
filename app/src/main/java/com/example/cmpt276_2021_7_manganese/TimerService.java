package com.example.cmpt276_2021_7_manganese;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * This TimerService class is used as a method to keep the timer running
 * in the background so that the user is able to use other apps during the timer
 * countdown. Also helps to update the timer os screen when coming back to screen
 * after activity was destroyed.
 * @author Rio Samson
 */
public class TimerService extends Service {
    static final String INTENT_IS_RUNNING_KEY = "runningTimer";
    static private final String INTENT_TIME_LEFT_KEY = "timeRemaining";
    static private final String INTENT_IS_FINISHED_KEY = "timerDone";
    private static final String BROADCAST_FTR = "com.example.cmpt276_2021_7_manganese.countdown_broadcast";
    private final String IS_RESET = "hasBeenReset";
    private final int DEFAULT_TIME_LEFT = 0;
    private final int ONE_SECOND_IN_MILLI = 1000;
    private long[] vibrationPattern = {0, 1000, 1000};
    private int indexVibrateRepeat = 1;
    private long timerStartTime;
    private static boolean isTimerRunning = false;
    private static long timeLeft;
    private CountDownTimer countDownTimer;
    private static boolean isTimerDone = true;
    private MediaPlayer player;
    private Vibrator vibrator;
    private NotificationManagerCompat notificationManager;
    public static final String CHANNEL_1_ID = "TimerDone";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_FTR));
        timeLeft = intent.getLongExtra(INTENT_TIME_LEFT_KEY, DEFAULT_TIME_LEFT);
        isTimerRunning = true;
        isTimerDone = false;
        countDownTimer = new CountDownTimer(timeLeft, ONE_SECOND_IN_MILLI) {
            @Override
            public void onTick(long mSecondsToFinish) {
                timeLeft = mSecondsToFinish;
            }

            @Override
            public void onFinish() {
                timeLeft = timerStartTime;
                isTimerRunning = false;
                isTimerDone = true;
                alarmBlast();
            }
        }.start();
        isTimerRunning = true;

        return START_STICKY;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onDestroy();
        }
    };

    private void alarmBlast() {
        player = MediaPlayer.create(this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        player.setLooping(true);
        player.start();
        vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, indexVibrateRepeat));

        timeoutNotificationSend(null);
    }

    public void timeoutNotificationSend(View v) {
        notificationManager = NotificationManagerCompat.from(this);

        Intent timerDoneIntent = new Intent(this, TimeoutTimerActivity.class);
        PendingIntent timerPendingIntent = PendingIntent.getActivity(this, 0
                , timerDoneIntent, 0);

        Intent intentForBroadcast = new Intent(BROADCAST_FTR);
        PendingIntent intentForBroadcastPending = PendingIntent.getBroadcast(this, 0
                , intentForBroadcast, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentTitle(getString(R.string.timeout_timer_done)).setContentText(getString(R.string.timer_has_run_out))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(timerPendingIntent)
                .setAutoCancel(true)
                .addAction(R.mipmap.ic_launcher, getString(R.string.stop), intentForBroadcastPending);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }

    public static Intent makeLaunchIntent(Context c) {
        return new Intent(c, TimerService.class);
    }

    public static Intent makeStatusIntent(Context c) {
        Intent intent = new Intent(c, TimerService.class);
        intent.putExtra(INTENT_TIME_LEFT_KEY, timeLeft);
        intent.putExtra(INTENT_IS_RUNNING_KEY, isTimerRunning);
        intent.putExtra(INTENT_IS_FINISHED_KEY, isTimerDone);
        return intent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent statusIntent = TimeoutTimerActivity.makeResetIntentForService(TimerService.this);
        if (statusIntent.getBooleanExtra(IS_RESET, true)) {
            isTimerDone = true;
        }
        isTimerRunning = false;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (player != null && player.isPlaying()) {
            player.stop();
        }
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
