package shavkunov.skorogovorun.lite.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shavkunov.skorogovorun.lite.PatterTask;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.RecyclerViewAdapter;
import shavkunov.skorogovorun.lite.model.Patter;

public class TongueTwistersActivity extends AppCompatActivity {

    private static final String KEY_LAST_PATTER = "lastPatter";
    public static final String EXTRA_DATE_TONGUE = "extraDateTongue";

    private SharedPreferences sharedPreferences;
    private List<Patter> patters;
    private boolean isInternet;
    private PatterTask task;

    @BindView(R.id.tongue_scroll_view)
    DiscreteScrollView tongueScrollView;

    @BindView(R.id.image_empty)
    ImageView noInternetImage;

    @BindView(R.id.title_empty)
    TextView noInternetTitle;

    @BindView(R.id.subtitle_empty)
    TextView noInternetSubtitle;

    @BindView(R.id.button_empty)
    FloatingActionButton noInternetButton;

    @BindView(R.id.progress)
    DilatingDotsProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongue_twisters);
        patters = new ArrayList<>();
        ButterKnife.bind(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        task = (PatterTask) new PatterTask().execute();
        getPattersFromInternet();
    }

    public void getPattersFromInternet() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.showNow();

                if (task.getStatus().toString().equals("FINISHED")) {
                    progressBar.hideNow();

                    if (task.getPatters() != null) {
                        Collections.addAll(patters, task.getPatters());
                        setTongueRecyclerView();
                        isInternet = true;
                    } else {
                        noInternetImage.setVisibility(View.VISIBLE);
                        noInternetTitle.setVisibility(View.VISIBLE);
                        noInternetSubtitle.setVisibility(View.VISIBLE);

                        noInternetImage.setImageResource(R.drawable.no_internet);
                        noInternetTitle.setText(R.string.no_connection);
                        noInternetSubtitle.setText(R.string.check_your_connection);
                        noInternetButton.setVisibility(View.VISIBLE);
                        noInternetButton.setImageResource(R.drawable.refresh);
                    }
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }

    /**
     * Проверяет, подключено ли устройство к интернету
     *
     * @return true, если устройство имеет доступ к сети
     */
    public boolean isConnectedToNetwork() {
        boolean connected = false;
        ConnectivityManager cm = (ConnectivityManager) this
                .getSystemService(CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null) {
                connected = ni.isConnected();
            }
        }

        return connected;
    }

    @OnClick(R.id.button_empty)
    public void onNoInternetButton() {
        if (isConnectedToNetwork()) {
            noInternetButton.setVisibility(View.GONE);
        }

        noInternetImage.setVisibility(View.INVISIBLE);
        noInternetTitle.setVisibility(View.INVISIBLE);
        noInternetSubtitle.setVisibility(View.INVISIBLE);

        task = (PatterTask) new PatterTask().execute();
        getPattersFromInternet();
    }


    private void setTongueRecyclerView() {
        tongueScrollView.setAdapter(new RecyclerViewAdapter(this, patters, true));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (task != null) {
            task.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE_TONGUE, new Date().getTime());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}