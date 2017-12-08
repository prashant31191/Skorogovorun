package shavkunov.skorogovorun.lite.controller.tabs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;
import shavkunov.skorogovorun.lite.NotificationReceiver;
import shavkunov.skorogovorun.lite.R;

import static android.content.Context.ALARM_SERVICE;

public class SettingsFragment extends Fragment {

    private static final String LOG_ALARM_OFF = "AlarmManagerOff";
    private static final String LOG_ALARM_SET = "AlarmManagerWorked";
    private static final String SAVED_ONE_TIME = "OnceTime";
    private static final String SAVED_MILLIS = "Millis";
    private static final String SAVED_CHECKED = "Checked";

    private Unbinder unbinder;
    private SharedPreferences preferences;

    private boolean isOneTime;
    private long millis;

    @BindView(R.id.time_settings)
    Button timeSettings;

    @BindView(R.id.image_settings)
    ImageView imageSettings;

    @BindView(R.id.switch_settings)
    Switch switchSettings;

    public static Fragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        unbinder = ButterKnife.bind(this, view);
        switchSettings.setChecked(preferences.getBoolean(SAVED_CHECKED, false));
        isOneTime = preferences.getBoolean(SAVED_ONE_TIME, true);
        millis = preferences.getLong(SAVED_MILLIS, 0);
        setViews(millis);
        return view;
    }

    @OnCheckedChanged(R.id.switch_settings)
    public void onSwitchSettings(boolean checked) {
        int color;
        if (checked) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            setReminder(getContext(), NotificationReceiver.class,
                    calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            color = R.color.colorTimeOn;
        } else {
            cancelReminder(getContext(), NotificationReceiver.class);
            color = R.color.colorTimeOff;
            Log.d(LOG_ALARM_OFF, "Alarm Manager was turned off");
        }

        timeSettings.setTextColor(getResources().getColor(color));
    }

    private void setReminder(Context context, Class<?> cls, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(Calendar.HOUR_OF_DAY, hour);
        setCalendar.set(Calendar.MINUTE, min);
        setCalendar.set(Calendar.SECOND, 0);
        millis = setCalendar.getTimeInMillis();
        setViews(millis);

        cancelReminder(context, cls);

        if (setCalendar.before(calendar)) {
            setCalendar.add(Calendar.DATE, 1);
        }

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                10, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (am != null) {
            am.setRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d(LOG_ALARM_SET, "AlarmManager worked");
        }
    }

    private void cancelReminder(Context context, Class<?> cls) {
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (am != null) {
            am.cancel(pendingIntent);
        }
        pendingIntent.cancel();
    }

    @OnClick(R.id.time_settings)
    public void onTimeSettingsClick() {
        LinearTimePickerDialog dialog = LinearTimePickerDialog.Builder
                .with(getContext())
                .setDialogBackgroundColor(getResources().getColor(R.color.colorDialogPickerBackground))
                .setPickerBackgroundColor(getResources().getColor(R.color.colorPickerBackground))
                .setLineColor(getResources().getColor(R.color.colorLineColor))
                .setTextColor(getResources().getColor(R.color.colorWhite))
                .setShowTutorial(isOneTime)
                .setTextBackgroundColor(getResources().getColor(R.color.colorTextBackground))
                .setButtonCallback(new LinearTimePickerDialog.ButtonCallback() {
                    @Override
                    public void onPositive(DialogInterface dialog, int hour, int minutes) {
                        isOneTime = false;
                        switchSettings.setChecked(true);
                        setReminder(getContext(), NotificationReceiver.class,
                                hour, minutes);
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.cancel();
                    }
                })
                .build();
        dialog.show();
    }

    private void setViews(long millis) {
        Calendar calendar = Calendar.getInstance();

        long m;
        if (millis == 0) {
            m = System.currentTimeMillis();
            this.millis = m;
        } else {
            m = millis;
        }
        calendar.setTimeInMillis(m);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String minute;
        if (calendar.get(Calendar.MINUTE) < 10) {
            minute = "0" + calendar.get(Calendar.MINUTE);
        } else {
            minute = String.valueOf(calendar.get(Calendar.MINUTE));
        }

        String result = String.valueOf(new StringBuilder()
                .append(hour).append(":").append(minute));
        timeSettings.setText(result);

        int imageRes;
        if (hour >= 6 && hour < 21) {
            imageRes = R.drawable.afternoon;
        } else {
            imageRes = R.drawable.night;
        }
        imageSettings.setImageResource(imageRes);

        if (!switchSettings.isChecked()) {
            timeSettings.setTextColor(getResources().getColor(R.color.colorTimeOff));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.edit().putBoolean(SAVED_CHECKED, switchSettings.isChecked()).apply();
        preferences.edit().putLong(SAVED_MILLIS, millis).apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        preferences.edit().putBoolean(SAVED_ONE_TIME, isOneTime).apply();
    }
}
