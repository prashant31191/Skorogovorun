package shavkunov.skorogovorun.lite.controller;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.RecyclerViewAdapter;
import shavkunov.skorogovorun.lite.model.DatabaseLab;
import shavkunov.skorogovorun.lite.model.Patter;

public class FavoriteTongueActivity extends AppCompatActivity {

    private static final String KEY_LAST_PATTER_FAVORITE = "lastPatterFavorite";

    private List<Patter> patters;
    private SharedPreferences preferences;

    @BindView(R.id.tongue_scroll_view)
    DiscreteScrollView favoriteTongueScrollView;

    @BindView(R.id.image_empty)
    ImageView emptyFavoriteImage;

    @BindView(R.id.title_empty)
    TextView emptyFavoriteTitle;

    @BindView(R.id.subtitle_empty)
    TextView emptyFavoriteSubtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongue_twisters);
        ButterKnife.bind(this);
        patters = DatabaseLab.get(this).getPatters();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (patters.size() > 0) {
            setScrollView();
        } else {
            Glide.with(this)
                    .load(R.drawable.zoom)
                    .into(emptyFavoriteImage);
            emptyFavoriteTitle.setText(R.string.favorite_title);
            emptyFavoriteSubtitle.setText(R.string.favorite_tongue_subtitle);
        }
    }

    private void setScrollView() {
        favoriteTongueScrollView.setAdapter(new RecyclerViewAdapter(this, patters, false));
        favoriteTongueScrollView.scrollToPosition(preferences.getInt(KEY_LAST_PATTER_FAVORITE, 0));
        favoriteTongueScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.8f)
                .build());
    }

    @Override
    protected void onPause() {
        super.onPause();

        int lastPosition = favoriteTongueScrollView.getCurrentItem();
        preferences.edit().putInt(KEY_LAST_PATTER_FAVORITE, lastPosition).apply();
    }
}
