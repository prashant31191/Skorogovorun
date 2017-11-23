package shavkunov.skorogovorun.lite.controller;

import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.controller.tabs.ExercisesFragment;
import shavkunov.skorogovorun.lite.model.Patter;

public class TongueTwistersActivity extends AppCompatActivity {

    private static final String KEY_LAST_PATTER = "lastPatter";

    private SharedPreferences sharedPreferences;
    private List<Patter> patters;
    private boolean isInternet;

    @BindView(R.id.tongue_scroll_view)
    DiscreteScrollView tongueScrollView;

    @BindView(R.id.no_internet_image)
    ImageView noInternetImage;

    @BindView(R.id.no_internet_title)
    TextView noInternetTitle;

    @BindView(R.id.no_internet_subtitle)
    TextView noInternetSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongue_twisters);

        patters = new ArrayList<>();
        Parcelable[] p = getIntent().getParcelableArrayExtra(ExercisesFragment.EXTRA_PATTERS);
        if (p != null) {
            for (Parcelable parcelable : p) {
                patters.add((Patter) parcelable);
            }
            isInternet = true;
        }

        ButterKnife.bind(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (patters.size() != 0) {
            setTongueRecyclerView();
        } else {
            Glide.with(this)
                    .load(R.drawable.no_internet)
                    .into(noInternetImage);
            noInternetTitle.setText(R.string.no_connection);
            noInternetSubtitle.setText(R.string.check_your_connection);
        }
    }

    private void setTongueRecyclerView() {
        tongueScrollView.setAdapter(new TongueAdapter());
        tongueScrollView.scrollToPosition(sharedPreferences.getInt(KEY_LAST_PATTER, 0));
        tongueScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.8f)
                .build());
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isInternet) {
            int lastPosition = tongueScrollView.getCurrentItem();
            sharedPreferences.edit().putInt(KEY_LAST_PATTER, lastPosition).apply();
        }
    }

    public class TongueHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tCard_favorite_button)
        ShineButton tCardFavoriteButton;

        @BindView(R.id.tCard_image)
        ImageView tCardImage;

        @BindView(R.id.tCard_title)
        TextView tCardTitle;

        @BindView(R.id.tCard_sounds)
        TextView tCardSounds;

        public TongueHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tCardFavoriteButton.init(TongueTwistersActivity.this);
        }
    }

    public class TongueAdapter extends RecyclerView.Adapter<TongueHolder> {

        @Override
        public TongueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(TongueTwistersActivity.this);
            View view = inflater.inflate(R.layout.card_tongue, parent, false);
            return new TongueHolder(view);
        }

        @Override
        public void onBindViewHolder(TongueHolder holder, int position) {
            Glide.with(TongueTwistersActivity.this)
                    .load(patters.get(position).getImage())
                    .apply(new RequestOptions()
                            .error(R.drawable.error)
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.tCardImage);
            holder.tCardTitle.setText(patters.get(position).getTitle());
            String sounds = getString(R.string.letters) + " " +
                    patters.get(position).getSounds();
            holder.tCardSounds.setText(sounds);
        }

        @Override
        public int getItemCount() {
            return patters.size();
        }
    }
}