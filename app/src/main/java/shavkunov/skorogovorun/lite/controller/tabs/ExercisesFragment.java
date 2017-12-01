package shavkunov.skorogovorun.lite.controller.tabs;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.controller.CardLab;
import shavkunov.skorogovorun.lite.controller.FavoriteTongueActivity;
import shavkunov.skorogovorun.lite.controller.MainActivity;
import shavkunov.skorogovorun.lite.controller.TongueTwistersActivity;

public class ExercisesFragment extends Fragment {

    private static final String SAVED_LAST_VISIT = "savedLastVisit";

    public static final int REQUEST_FAVORITE_TONGUE_ACTIVITY = 0;
    public static final int REQUEST_TONGUE_ACTIVITY = 1;

    private Unbinder unbinder;
    private long lastDate;
    private Runnable runnableCount;
    private Handler handler;
    private SharedPreferences preferences;

    @BindView(R.id.exercises_recycler_view)
    RecyclerView exercisesRecyclerView;

    public static Fragment newInstance() {
        return new ExercisesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        lastDate = preferences.getLong(SAVED_LAST_VISIT, 0);
        Bundle argumentsBundle = getArguments();
        if (argumentsBundle != null) {
            if (argumentsBundle.getLong(MainActivity.LAST_DATE) > 0) {
                lastDate = argumentsBundle.getLong(MainActivity.LAST_DATE);
            }
        }

        setExercisesRecyclerView();
        return view;
    }

    private void setExercisesRecyclerView() {
        exercisesRecyclerView.setNestedScrollingEnabled(false);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        exercisesRecyclerView.setAdapter(new ExercisesAdapter());
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.edit().putLong(SAVED_LAST_VISIT, lastDate).apply();

        if (handler != null) {
            handler.removeCallbacks(runnableCount);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (handler != null && runnableCount != null) {
            handler.post(runnableCount);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class ExercisesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_image_default)
        ImageView eCardImage;

        @BindView(R.id.card_title_default)
        TextView eCardTitle;

        @BindView(R.id.card_button_default)
        ShineButton eCardButton;

        @BindView(R.id.card_favorite_default)
        ShineButton eCardFavoriteButton;

        @BindView(R.id.card_subtitle_default)
        TextView eCardDate;

        public ExercisesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            eCardDate.setVisibility(View.VISIBLE);
        }
    }

    public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesHolder> {

        @Override
        public ExercisesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.card_default, parent, false);
            return new ExercisesHolder(view);
        }

        @Override
        public void onBindViewHolder(final ExercisesHolder holder, int position) {
            switch (position) {
                case 0:
                    holder.eCardTitle.setText(R.string.tongue_twisters);
                    CardLab.newInstance().setImage(holder.itemView, R.drawable.forest,
                            holder.eCardImage);

                    handler = new Handler();
                    setRunnableCount(holder);
                    handler.post(runnableCount);

                    holder.eCardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CardLab.newInstance().setIntent(getActivity(),
                                    TongueTwistersActivity.class, REQUEST_TONGUE_ACTIVITY);
                        }
                    });
                    holder.eCardFavoriteButton.setVisibility(View.VISIBLE);
                    holder.eCardFavoriteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CardLab.newInstance().setIntent(getActivity(),
                                    FavoriteTongueActivity.class, REQUEST_FAVORITE_TONGUE_ACTIVITY);
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

    /**
     * Высчитывает, когда был последний визит в одну из вьюшек
     *
     * @return строку с результатом
     */
    private String lastVisit() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
        long time = System.currentTimeMillis() - lastDate;

        long seconds = time / 1000;
        long minutes = time / (60 * 1000) % 60;
        long hours = time / (60 * 60 * 1000) % 24;

        String result;

        if (seconds < 0) {
            result = " ";
        } else if (seconds < 60) {
            result = String.valueOf(new StringBuilder()
                    .append(getString(R.string.last_visit)).append(" ")
                    .append(getString(R.string.less_than_a_minute)).append(" ")
                    .append(getString(R.string.ago)));
        } else if (seconds < 3600) {
            result = String.valueOf(new StringBuilder()
                    .append(getString(R.string.last_visit)).append(" ")
                    .append(minutes).append(" ").append(getString(R.string.minute))
                    .append(" ").append(getString(R.string.ago)));
        } else if (seconds < 7200) {
            result = String.valueOf(new StringBuilder()
                    .append(getString(R.string.last_visit)).append(" ")
                    .append(hours).append(" ").append(getString(R.string.hour))
                    .append(" ").append(minutes).append(" ").append(getString(R.string.minute))
                    .append(" ").append(getString(R.string.ago)));
        } else if (seconds < 86400) {
            result = String.valueOf(new StringBuilder()
                    .append(getString(R.string.last_visit)).append(" ")
                    .append(hours).append(" ").append(getString(R.string.hour_two))
                    .append(" ").append(minutes).append(" ").append(getString(R.string.minute))
                    .append(" ").append(getString(R.string.ago)));
        } else if (seconds < 172800) {
            result = String.valueOf(new StringBuilder()
                    .append(getString(R.string.last_visit)).append(" ")
                    .append(getString(R.string.yesterday)));
        } else if (seconds < 259200) {
            result = String.valueOf(new StringBuilder()
                    .append(getString(R.string.last_visit)).append(" ")
                    .append(getString(R.string.the_day_before_yesterday)));
        } else {
            result = String.valueOf(new StringBuilder()
                    .append(getString(R.string.last_visit)).append(" ")
                    .append(dateFormat.format(new Date(lastDate))));
        }

        String mistake = String.valueOf(new StringBuilder()
                .append(getString(R.string.last_visit)).append(" ")
                .append("1 янв. 1970"));

        if (result.equals(mistake)) {
            result = " ";
        }

        return result;
    }

    private void setRunnableCount(final ExercisesHolder holder) {
        runnableCount = new Runnable() {
            @Override
            public void run() {
                Activity activity = getActivity();

                if (activity != null) {
                    holder.eCardDate.setText(lastVisit());
                }

                handler.postDelayed(this, 1000);
            }
        };
    }
}
