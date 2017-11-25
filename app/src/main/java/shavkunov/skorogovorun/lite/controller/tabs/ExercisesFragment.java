package shavkunov.skorogovorun.lite.controller.tabs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.controller.FavoriteTongueActivity;
import shavkunov.skorogovorun.lite.controller.TongueTwistersActivity;

public class ExercisesFragment extends Fragment {

    private static final String SAVED_LAST_VISIT = "savedLastVisit";

    private static final int REQUEST_FAVORITE_TONGUE_ACTIVITY = 0;
    private static final int REQUEST_TONGUE_ACTIVITY = 1;

    private Unbinder unbinder;
    private long lastDate;
    private ExercisesAdapter adapter;
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

        setExercisesRecyclerView();
        return view;
    }

    private void setExercisesRecyclerView() {
        adapter = new ExercisesAdapter();
        exercisesRecyclerView.setNestedScrollingEnabled(false);
        exercisesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        exercisesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onPause() {
        super.onPause();
        preferences.edit().putLong(SAVED_LAST_VISIT, lastDate).apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnableCount);
    }

    public class ExercisesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.eCard_image)
        ImageView eCardImage;

        @BindView(R.id.eCard_title)
        TextView eCardTitle;

        @BindView(R.id.eCard_button)
        FloatingActionButton eCardButton;

        @BindView(R.id.eCard_favorite_button)
        FloatingActionButton eCardFavoriteButton;

        @BindView(R.id.eCard_date)
        TextView eCardDate;

        private View itemView;

        public ExercisesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;
        }
    }

    public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesHolder> {

        @Override
        public ExercisesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.card_exercises, parent, false);
            return new ExercisesHolder(view);
        }

        @Override
        public void onBindViewHolder(final ExercisesHolder holder, int position) {
            switch (position) {
                case 0:
                    holder.eCardTitle.setText(R.string.tongue_twisters);
                    setImage(holder, R.drawable.forest);

                    handler = new Handler();
                    runnableCount = new Runnable() {
                        @Override
                        public void run() {
                            holder.eCardDate.setText(lastVisit());

                            handler.postDelayed(this, 1000);
                        }
                    };
                    handler.post(runnableCount);

                    holder.eCardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setIntent(TongueTwistersActivity.class,
                                    REQUEST_TONGUE_ACTIVITY);
                        }
                    });
                    holder.eCardFavoriteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setIntent(FavoriteTongueActivity.class,
                                    REQUEST_FAVORITE_TONGUE_ACTIVITY);
                        }
                    });

                    break;
            }
        }

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

            return result;
        }

        private void setIntent(Class<?> cls, int requestCode) {
            Intent intent = new Intent(getContext(), cls);
            startActivityForResult(intent, requestCode);
        }

        private void setImage(ExercisesHolder holder, @DrawableRes int imageRes) {
            Glide.with(holder.itemView)
                    .load(imageRes)
                    .apply(RequestOptions.bitmapTransform(
                            new ColorFilterTransformation(Color.argb(210, 66, 66, 66)))
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL))
                    .into(holder.eCardImage);
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TONGUE_ACTIVITY:
                    lastDate = data.getLongExtra(TongueTwistersActivity.EXTRA_DATE_TONGUE, 0);
                    break;
                case REQUEST_FAVORITE_TONGUE_ACTIVITY:
                    lastDate = data.getLongExtra(FavoriteTongueActivity.EXTRA_DATE_FAVORITE, 0);
                    break;
            }

            adapter.notifyDataSetChanged();
        }
    }
}
