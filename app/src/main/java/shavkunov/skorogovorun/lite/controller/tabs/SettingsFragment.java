package shavkunov.skorogovorun.lite.controller.tabs;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.controller.MainActivity;

public class SettingsFragment extends Fragment {

    private static final String CHANNEL_ID_NOTIFICATION = "channelNotification";

    private Unbinder unbinder;

    @BindView(R.id.settings_recycler_view)
    RecyclerView settingsRecyclerView;

    public static Fragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        setSettingsRecyclerView();
        return view;
    }

    private void setSettingsRecyclerView() {
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        settingsRecyclerView.setAdapter(new SettingsAdapter());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private String getRandomTextForNotification() {
        Activity activity = getActivity();

        if (activity != null) {
            String[] texts = activity.getResources().getStringArray(R.array.text_for_notification);
            int index = new Random().nextInt(texts.length);
            return texts[index];
        }

        return " ";
    }

    public class SettingsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_button_settings)
        Button cardButtonSettings;

        public SettingsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class SettingsAdapter extends RecyclerView.Adapter<SettingsHolder> {

        @Override
        public SettingsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.card_settings, parent, false);
            return new SettingsHolder(view);
        }

        @Override
        public void onBindViewHolder(SettingsHolder holder, int position) {
            switch (position) {
                case 0:
                    holder.cardButtonSettings.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Context context = getContext();

                            if (context != null) {
                                String contentText = getRandomTextForNotification();

                                Intent intent = new Intent(context, MainActivity.class);
                                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                                        10, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                                NotificationCompat.Builder builder =
                                        new NotificationCompat.Builder(context,
                                                CHANNEL_ID_NOTIFICATION)
                                                .setContentIntent(pendingIntent)
                                                .setSmallIcon(R.drawable.icon_notification)
                                                .setColor(context.getResources()
                                                        .getColor(R.color.colorNotification))
                                                .setContentTitle(getString(R.string.app_name))
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
                        }
                    });
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }
}
