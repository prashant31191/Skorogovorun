package shavkunov.skorogovorun.lite.controller;

import android.content.SharedPreferences;
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
import com.sackcentury.shinebuttonlib.ShineButton;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;
import shavkunov.skorogovorun.lite.R;

public class TongueTwistersActivity extends AppCompatActivity {

    private static final String KEY_LAST_PATTER = "lastPatter";

    private SharedPreferences sharedPreferences;

    @BindView(R.id.tongue_scroll_view)
    DiscreteScrollView tongueScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongue_twisters);
        ButterKnife.bind(this);
        setTongueRecyclerView();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        int lastPosition = sharedPreferences.getInt(KEY_LAST_PATTER, 0);
        if (lastPosition != 0) {
            tongueScrollView.smoothScrollToPosition(lastPosition);
        }
    }

    private void setTongueRecyclerView() {
        InfiniteScrollAdapter wrapperAdapter = InfiniteScrollAdapter.wrap(new TongueAdapter());
        tongueScrollView.setAdapter(wrapperAdapter);
        tongueScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.8f)
                .build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        int lastPosition = tongueScrollView.getCurrentItem();
        sharedPreferences.edit().putInt(KEY_LAST_PATTER, lastPosition).apply();
    }

    public class TongueHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tCard_favorite_button)
        ShineButton tCardFavoriteButton;

        @BindView(R.id.tCard_image)
        ImageView tCardImage;

        @BindView(R.id.tCard_title)
        TextView tCardTitle;

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
            // На время, для тестирования
            Glide.with(TongueTwistersActivity.this)
                    .load(R.drawable.forest)
                    .into(holder.tCardImage);

            String title;

            switch (position) {
                case 0:
                    title = "Тридцать три корабля лавировали лавировали, да не вылавировали";
                    break;
                case 1:
                    title = "Шла Саша по шоссе и сосала сушку";
                    break;
                default:
                    title = "Шли сорок мышей нашли сорок грошей";
            }
            holder.tCardTitle.setText(title);
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}
