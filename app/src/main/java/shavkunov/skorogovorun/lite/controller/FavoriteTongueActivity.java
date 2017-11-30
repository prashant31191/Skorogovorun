package shavkunov.skorogovorun.lite.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.TongueTwistersAdapter;
import shavkunov.skorogovorun.lite.database.DatabaseLab;
import shavkunov.skorogovorun.lite.model.Patter;

public class FavoriteTongueActivity extends AppCompatActivity {

    private static final String SAVED_LAST_PATTER_FAVORITE = "lastPatterFavorite";
    public static final String EXTRA_DATE_FAVORITE = "extraDateFavorite";

    private List<Patter> patters;

    private SharedPreferences preferences;
    private TongueTwistersAdapter adapter;

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
        patters = DatabaseLab.getInstance(this).getPatters();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (patters.size() > 0) {
            setScrollView();
        } else {
            showHideEmptyViews();
        }
    }

    private void showHideEmptyViews() {
        emptyFavoriteImage.setImageResource(R.drawable.zoom);
        emptyFavoriteTitle.setText(R.string.favorite_title);
        emptyFavoriteSubtitle.setText(R.string.add_one_patter);
    }

    private void setScrollView() {
        adapter = new TongueTwistersAdapter(this, patters, false);
        favoriteTongueScrollView.setAdapter(adapter);

        String lastPatterTitle = preferences.getString(SAVED_LAST_PATTER_FAVORITE, "");
        for (int i = 0; i < patters.size(); i++) {
            if (patters.get(i).getTitle().equals(lastPatterTitle)) {
                favoriteTongueScrollView.scrollToPosition(i);
            }
        }

        favoriteTongueScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.8f)
                .build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (patters.size() > 0) {
            int lastPosition = favoriteTongueScrollView.getCurrentItem();
            String lastPatterTitle = patters.get(lastPosition).getTitle();
            preferences.edit().putString(SAVED_LAST_PATTER_FAVORITE, lastPatterTitle).apply();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE_FAVORITE, new Date().getTime());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                if (patters.size() > 1) {
                    Collections.shuffle(patters);
                    DatabaseLab.getInstance(this).deletePatters();
                    DatabaseLab.getInstance(this).addPatters(patters);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, getString(R.string.shuffle_is_impossible),
                            Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_clear:
                String result;

                if (patters.size() > 0) {
                    DatabaseLab.getInstance(this).deletePatters();
                    patters.clear();
                    adapter.notifyDataSetChanged();
                    showHideEmptyViews();
                    result = getString(R.string.everything_deleted);
                } else {
                    result = getString(R.string.nothing_to_delete);
                }

                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_to_start:
                String toastText = null;

                if (patters.size() == 0) {
                    toastText = getString(R.string.no_patters);
                } else if (patters.size() == 1){
                    toastText = getString(R.string.little_patters);
                } else {
                    favoriteTongueScrollView.smoothScrollToPosition(0);
                }

                if (toastText != null) {
                    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
