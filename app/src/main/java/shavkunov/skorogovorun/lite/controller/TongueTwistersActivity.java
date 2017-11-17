package shavkunov.skorogovorun.lite.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import shavkunov.skorogovorun.lite.R;

public class TongueTwistersActivity extends AppCompatActivity {

    @BindView(R.id.tongue_recycler_view)
    RecyclerView tongueRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongue_twisters);
        ButterKnife.bind(this);
        setTongueRecyclerView();
    }

    private void setTongueRecyclerView() {
        //tongueRecyclerView.setLayoutManager();
    }
}
