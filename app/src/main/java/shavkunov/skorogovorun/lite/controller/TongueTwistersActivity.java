package shavkunov.skorogovorun.lite.controller;

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

    @BindView(R.id.tongue_scroll_view)
    DiscreteScrollView tongueScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongue_twisters);
        ButterKnife.bind(this);
        setTongueRecyclerView();
    }

    private void setTongueRecyclerView() {
        InfiniteScrollAdapter wrapperAdapter = InfiniteScrollAdapter.wrap(new TongueAdapter());
        tongueScrollView.setAdapter(wrapperAdapter);
        tongueScrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.0f)
                .setMinScale(0.8f)
                .build());
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
            Glide.with(TongueTwistersActivity.this)
                    .load(R.drawable.forest)
                    .into(holder.tCardImage);
            holder.tCardTitle.setText("Тридцать три корабля лавировали лавировали, да не вылавировали");
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }
}
