package shavkunov.skorogovorun.lite.controller.tabs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import shavkunov.skorogovorun.lite.NotificationReceiver;
import shavkunov.skorogovorun.lite.R;

public class SettingsFragment extends Fragment {

    private static final String LOG_ALARM_SET = "alarmManagerWorked";

    private Unbinder unbinder;

    public static Fragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.card_button_settings)
    public void onClickButtonSettings() {
        Context context = getContext();

        if (context != null) {
            AlarmManager alarmManager = (AlarmManager)
                    context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    10, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000,
                        pendingIntent);
                Log.d(LOG_ALARM_SET, "alarmManager worked");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
