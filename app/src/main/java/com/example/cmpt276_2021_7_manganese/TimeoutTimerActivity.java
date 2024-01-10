//Resource used: https://www.youtube.com/watch?v=MDuGwI6P-X8
//more resource taken from: https://codinginflow.com/tutorials/android/countdowntimer/part-2-configuration-changes
//https://www.youtube.com/watch?v=sOwqYNdi_x8
//https://www.youtube.com/watch?v=YsHHXg1vbcc
package com.example.cmpt276_2021_7_manganese;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

/**
 * This TimeoutTimer activity represents the screen containing timeout features.
 * Features such as starting the timer, pausing the timer, resuming the timer, reseting the timer
 * once done. Plays sound and vibrate once timer is done. Timer runs in background and can use
 * other applications.
 * @author Rio Samson
 */
public class TimeoutTimerActivity extends AppCompatActivity {
    static private final String PREFS_TAG = "Time Settings";
    static private final String SAVE_TIMER_KEY = "Different timer settings";
    private final String INTENT_IS_RUNNING_KEY = "runningTimer";
    private final String INTENT_TIME_LEFT_KEY = "timeRemaining";
    static private final String INTENT_IS_FINISHED_KEY = "timerDone";
    private static final String IS_RESET = "hasBeenReset";
    private final int DEFAULT_TIME_LEFT_INTENT = 0;
    private final int MIN_TO_MS_FACTOR = 60000;
    private final int MIN_TO_S_FACTOR = 60;
    private final int ONE_SECOND_IN_MILLI = 1000;
    private final int MILLI_TO_HOUR_FACTOR = 3600000;
    private final int SEC_TO_HOUR_FACTOR = 3600;
    private double timerSpeed;
    private double[] speeds = {0.25, 0.5, 0.75, 1, 2, 3, 4};
    int curSpeedIndex = 3;
    private TextView speedText;
    private int animationPro = 0;
    private ProgressBar progressBar;

    static private long timerStartTime;
    private boolean isTimerRunning;
    static private long timeLeft;
    private TextView timerClock;
    private CountDownTimer countDownTimer;
    private Button startPauseTimer;
    static private Intent serviceIntent;
    static private boolean isResetLastPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_timer);
        Toolbar toolbar = findViewById(R.id.timerToolBar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        timerClock = findViewById(R.id.timerClock);
        startPauseTimer = findViewById(R.id.startBtn);
        timerSpeed = loadSpeed();
        timerStartTime = (long) (loadSavedTimeStart());
        timeLeft = timerStartTime;
        serviceIntent = TimerService.makeLaunchIntent(TimeoutTimerActivity.this);
        speedText = findViewById(R.id.speed_text);
        progressBar = findViewById(R.id.progress_circle);
        checkRunningStatus();
        speedText.setText(getString(R.string.time_special) + (int)(timerSpeed*100) + "%");
        setupPreMadeTimerSettings();
        setupTimerClockWithButtons();
        setupCustomTimerSettings();
    }

    private void checkRunningStatus() {
        Intent statusIntent = TimerService.makeStatusIntent(TimeoutTimerActivity.this);
        boolean serviceDone = statusIntent.getBooleanExtra(INTENT_IS_FINISHED_KEY, true);
        SharedPreferences prefs = getSharedPreferences("isReset",
                MODE_PRIVATE);
        boolean wasResetLast = prefs.getBoolean("resetStatus", false);

        if (!serviceDone && (!wasResetLast)) {
            startPauseTimer.setText(R.string.resume);
            isTimerRunning = statusIntent.getBooleanExtra(INTENT_IS_RUNNING_KEY, false);
            timeLeft = statusIntent.getLongExtra(INTENT_TIME_LEFT_KEY, DEFAULT_TIME_LEFT_INTENT);
            if (isTimerRunning) {
                startTimer();
                isTimerRunning = true;
                startPauseTimer.setText(R.string.pause);
            }
        } else {
            if (timerSpeed != 1 || curSpeedIndex != 3) {
                timerSpeed = 1;
                curSpeedIndex = 3;
            }
        }
    }

    private void setupCustomTimerSettings() {
        EditText inputTime = findViewById(R.id.input_minutes);
        Button setTimeBtn = findViewById(R.id.set_time_btn);

        setTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customTime = inputTime.getText().toString();
                if (!customTime.isEmpty()) {
                    int time = Integer.parseInt(customTime);
                    stopTimer();
                    if (time != 0) {
                        timerStartTime = (long) time * MIN_TO_MS_FACTOR;
                        timeLeft = timerStartTime;
                    }
                    updateClock();
                    startPauseTimer.setText(R.string.start);
                    isTimerRunning = false;
                    inputTime.setText("");
                }
            }
        });
    }

    private void setupPreMadeTimerSettings() {
        RadioGroup radioGroup = findViewById(R.id.timeRadioGroup);
        int[] timerSettings = getResources().getIntArray(R.array.times);

        for(int i = 0; i < timerSettings.length; i++) {
            final int setting = timerSettings[i];
            RadioButton button = new RadioButton(this);
            button.setText("" + setting);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timerSpeed = 1.0;
                    curSpeedIndex = 3;
                    timerStartTime = (long) setting * MIN_TO_MS_FACTOR;
                    timeLeft = timerStartTime;
                    updateClock();
                    saveTimeSettings(setting);
                }
            });
            radioGroup.addView(button);
            int savedSetting = loadSavedData();
            if (setting == savedSetting) {
                button.setChecked(true);
            }
        }
    }

    private void setupTimerClockWithButtons() {
        Button resetTimer = findViewById(R.id.resetBtn);
        updateClock();

        startPauseTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTimerRunning) {
                    countDownTimer.cancel();
                    isResetLastPressed = false;
                    stopService(serviceIntent);
                    startPauseTimer.setText(R.string.resume);
                    isTimerRunning = false;
                } else {
                    startTimer();
                    serviceIntent.putExtra(INTENT_TIME_LEFT_KEY, timeLeft);
                    startService(serviceIntent);
                    startPauseTimer.setText(R.string.pause);
                    isResetLastPressed = false;
                    isTimerRunning = true;
                    updateIsLastResetStatus(false);
                }
            }
        });

        resetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerSpeed = 1.0;
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                timeLeft = timerStartTime;
                isResetLastPressed = true;
                if(!isTimerRunning) {
                    updateIsLastResetStatus(true);
                }
                stopService(serviceIntent);
                updateClock();
                startPauseTimer.setText(R.string.start);
                isTimerRunning = false;
                curSpeedIndex = 3;
                speedText.setText(getString(R.string.time_special) + (int)(timerSpeed*100) + "%");
            }
        });
    }

    private void updateIsLastResetStatus(boolean status) {
        SharedPreferences prefs = this.getSharedPreferences("isReset", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("resetStatus", status);
        editor.apply();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeft, (long)(ONE_SECOND_IN_MILLI / timerSpeed)) {
            @Override
            public void onTick(long mSecondsToFinish) {
                timeLeft = mSecondsToFinish;
                updateClock();
            }
            @Override
            public void onFinish() {
                timerSpeed = 1.0;
                curSpeedIndex = 3;
                timeLeft = timerStartTime;
                updateClock();
                startPauseTimer.setText(R.string.start);
                isTimerRunning = false;
                speedText.setText(getString(R.string.time_special) + (int)(timerSpeed*100) + "%");
            }
        }.start();
        isTimerRunning = true;
    }

    private void updateClock() {
        int hour = (int) ((timeLeft * timerSpeed) / MILLI_TO_HOUR_FACTOR);
        int min = (int) ((((timeLeft * timerSpeed) / ONE_SECOND_IN_MILLI) % SEC_TO_HOUR_FACTOR) / MIN_TO_S_FACTOR);
        int sec = (int) (((timeLeft * timerSpeed) / ONE_SECOND_IN_MILLI) % MIN_TO_S_FACTOR);
        int intStart = (int) (timerStartTime);
        progressBar.setMax(intStart);
        progressBar.setProgress(intStart - ((int) (intStart * ( (double )((double)(timeLeft * timerSpeed)/(double)timerStartTime)))));
        String display = String.format(Locale.getDefault(), "%d:%02d:%02d", hour, min, sec);
        timerClock.setText(display);
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private int loadSavedData() {
        SharedPreferences prefs = this.getSharedPreferences(PREFS_TAG,
                MODE_PRIVATE);
        return prefs.getInt(SAVE_TIMER_KEY, getResources().getInteger(R.integer.default_timer));
    }

    private void saveSpeed() {
        SharedPreferences prefs = this.getSharedPreferences("speedTag",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("timerSpeed", Double.doubleToRawLongBits(timerSpeed));
        editor.apply();
    }

    private double loadSpeed() {
        SharedPreferences prefs = this.getSharedPreferences("speedTag",
                MODE_PRIVATE);
        return Double.longBitsToDouble(prefs.getLong("timerSpeed", Double.doubleToLongBits(1)));
    }

    private void saveTimeSettings(int timerSettings) {
        SharedPreferences prefs = this.getSharedPreferences(PREFS_TAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(SAVE_TIMER_KEY, timerSettings);
        editor.apply();
    }

    public static Intent makeLaunchIntent(Context c) {
        return new Intent(c, TimeoutTimerActivity.class);
    }

    public static Intent makeResetIntentForService(Context c) {
        Intent intent =  new Intent(c, TimeoutTimerActivity.class);
        intent.putExtra(IS_RESET, isResetLastPressed);
        return intent;
    }

    public static Intent getServiceIntent(Context c) {
        return serviceIntent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSpeed();
        saveTimeStartSettings();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void saveTimeStartSettings() {
        SharedPreferences prefs = this.getSharedPreferences("prefs tag for time", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("timer start time", timerStartTime);
        editor.apply();
    }

    private long loadSavedTimeStart() {
        SharedPreferences prefs = this.getSharedPreferences("prefs tag for time",
                MODE_PRIVATE);
        return prefs.getLong("timer start time", (long) (getResources().getInteger(R.integer.default_timer) * MIN_TO_MS_FACTOR));
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.timeout_timer, menu);
        return true;
    }

    private void restartTimerWithNewSpeed() {
        countDownTimer.cancel();
        stopService(serviceIntent);
        timeLeft = (long) (timeLeft * timerSpeed);
        timerSpeed = speeds[curSpeedIndex];
        timeLeft = (long) (timeLeft/timerSpeed);
        startTimer();
        serviceIntent.putExtra(INTENT_TIME_LEFT_KEY, timeLeft);
        startService(serviceIntent);
        speedText.setText(getString(R.string.time_special) + (int)(timerSpeed*100) + "%");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_faster:
                if (isTimerRunning) {
                    if (curSpeedIndex < 6) {
                        curSpeedIndex++;
                        restartTimerWithNewSpeed();
                    }
                }
                return true;
            case R.id.action_slower:
                if (isTimerRunning) {
                    if (curSpeedIndex > 0) {
                        curSpeedIndex--;
                        restartTimerWithNewSpeed();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}