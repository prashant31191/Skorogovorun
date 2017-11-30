package shavkunov.skorogovorun.lite.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import shavkunov.skorogovorun.lite.Constants;
import shavkunov.skorogovorun.lite.PatterTask;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.TongueTwistersAdapter;
import shavkunov.skorogovorun.lite.database.DatabaseLab;
import shavkunov.skorogovorun.lite.model.Patter;

public class TongueTwistersActivity extends AppCompatActivity {

    private static final String SAVED_LAST_PATTER = "lastPatter";
    private static final String SAVED_LAST_CHOICE_ITEM = "lastChoiceItem";
    public static final String EXTRA_DATE_TONGUE = "extraDateTongue";

    private String[] arrayPatters = Constants.Url.ARRAY_PATTERS;
    private List<Patter> patters;

    private SharedPreferences sharedPreferences;
    private PatterTask task;
    private TongueTwistersAdapter adapter;

    private boolean isFirstVisit;
    private boolean isInternet;
    private int lastChoiceItem;

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

        lastChoiceItem = sharedPreferences.getInt(SAVED_LAST_CHOICE_ITEM, 0);
        task = new PatterTask();
        task.setUrl(arrayPatters[lastChoiceItem]);
        task.execute();
        getPattersFromInternet();
    }

    public void getPattersFromInternet() {
        if (patters.size() > 0) {
            patters.clear();

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }

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

                        // lastChoiceItem == 4 (Случайная сортировка)
                        if (lastChoiceItem == 4) {
                            Collections.shuffle(patters);
                            adapter.notifyDataSetChanged();
                        }
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

        noInternetImage.setVisibility(View.GONE);
        noInternetTitle.setVisibility(View.GONE);
        noInternetSubtitle.setVisibility(View.GONE);

        task = new PatterTask();
        task.setUrl(arrayPatters[lastChoiceItem]);
        task.execute();
        getPattersFromInternet();
    }

    private void setTongueRecyclerView() {
        adapter = new TongueTwistersAdapter(this, patters, true);
        tongueScrollView.setAdapter(adapter);

        if (!isFirstVisit) {
            tongueScrollView.scrollToPosition(sharedPreferences.getInt(SAVED_LAST_PATTER, 0));
            isFirstVisit = true;
        }

        tongueScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.8f)
                .build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isInternet) {
            int lastPosition = tongueScrollView.getCurrentItem();
            sharedPreferences.edit().putInt(SAVED_LAST_PATTER, lastPosition).apply();
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tongue_twisters, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                String[] sortingName = getResources().getStringArray(R.array.sorting_name);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.sort))
                        .setSingleChoiceItems(sortingName, lastChoiceItem,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        lastChoiceItem = i;
                                        sharedPreferences.edit().putInt(SAVED_LAST_CHOICE_ITEM,
                                                lastChoiceItem).apply();
                                    }
                                })
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (isConnectedToNetwork()) {
                                    task = new PatterTask();
                                    task.setUrl(arrayPatters[lastChoiceItem]);
                                    task.execute();
                                    getPattersFromInternet();
                                    setInvisibleNoInternetViews();
                                }
                            }
                        })
                        .show();
                return true;
            case R.id.action_add_all:
                String result;

                if (patters.size() > 0) {
                    DatabaseLab.getInstance(this).deletePatters();
                    DatabaseLab.getInstance(this).addPatters(patters);
                    result = getString(R.string.everything_added);
                    adapter.notifyDataSetChanged();
                } else {
                    result = getString(R.string.check_your_connection);
                }

                Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_to_start:
                if (patters.size() > 0) {
                    tongueScrollView.smoothScrollToPosition(0);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setInvisibleNoInternetViews() {
        noInternetImage.setVisibility(View.GONE);
        noInternetTitle.setVisibility(View.GONE);
        noInternetSubtitle.setVisibility(View.GONE);
        noInternetButton.setVisibility(View.GONE);
    }
}