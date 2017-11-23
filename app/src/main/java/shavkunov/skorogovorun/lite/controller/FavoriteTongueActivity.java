package shavkunov.skorogovorun.lite.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.RecyclerViewAdapter;
import shavkunov.skorogovorun.lite.model.DatabaseLab;
import shavkunov.skorogovorun.lite.model.Patter;

public class FavoriteTongueActivity extends AppCompatActivity {

    private List<Patter> patters;

    @BindView(R.id.tongue_scroll_view)
    DiscreteScrollView favoriteTongueScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongue_twisters);
        ButterKnife.bind(this);
        patters = DatabaseLab.get(this).getPatters();
        setScrollView();
    }

    private void setScrollView() {
        favoriteTongueScrollView.setAdapter(new RecyclerViewAdapter(this, patters, false));
        favoriteTongueScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.8f)
                .build());
        favoriteTongueScrollView.setItemAnimator(new SlideInUpAnimator());
    }
}
