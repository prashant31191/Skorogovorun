package shavkunov.skorogovorun.lite.controller;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import shavkunov.skorogovorun.lite.PatterTask;
import shavkunov.skorogovorun.lite.R;

public class SplashActivity extends AppCompatActivity {

    public static final String EXTRA_PATTERS = "patters";

    private PatterTask patterTask;

    @BindView(R.id.progress)
    DilatingDotsProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);
        patterTask = (PatterTask) new PatterTask().execute();
        progressBar.showNow();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                i.putExtra(EXTRA_PATTERS, patterTask.getPatters());
                startActivity(i);
                finish();
            }
        }, 3500);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (patterTask != null) {
            patterTask.cancel(true);
        }
    }
}
