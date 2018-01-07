package shavkunov.skorogovorun.lite.controller;

import android.app.Activity;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shavkunov.skorogovorun.lite.R;
import shavkunov.skorogovorun.lite.model.Card;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.TongueHolder> {

    private List<Card> cards;
    private Activity activity;
    private AdapterBehavior adapterBehavior;
    private CardsAdapter adapter;

    public CardsAdapter(Activity activity, List<Card> cards,
                        AdapterBehavior adapterBehavior) {
        this.activity = activity;
        this.cards = cards;
        this.adapterBehavior = adapterBehavior;
    }

    public void setCardsAdapter(CardsAdapter adapter) {
        this.adapter = adapter;
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
            tCardFavoriteButton.init(activity);
        }
    }

    @Override
    public TongueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.card_tongue, parent, false);
        return new TongueHolder(view);
    }

    @Override
    public void onBindViewHolder(final TongueHolder holder, int position) {
        holder.tCardTitle.setText(cards.get(position).getTitle());
        adapterBehavior.favoriteButton(holder, cards, activity, adapter);
        setImage(holder);
        setSounds(holder);
    }

    private void setImage(TongueHolder holder) {
        if (cards.get(holder.getAdapterPosition()).getImage() != null) {
            holder.tCardImage.setVisibility(View.VISIBLE);
            Glide.with(activity)
                    .load(cards.get(holder.getAdapterPosition()).getImage())
                    .apply(new RequestOptions()
                            .error(R.drawable.error)
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.tCardImage);
        } else {
            holder.tCardImage.setVisibility(View.GONE);
        }
    }

    private void setSounds(TongueHolder holder) {
        if (cards.get(holder.getAdapterPosition()).getSounds() != null) {
            holder.tCardSounds.setVisibility(View.VISIBLE);
            String sounds = activity.getString(R.string.letters) + " " +
                    cards.get(holder.getAdapterPosition()).getSounds();
            holder.tCardSounds.setText(sounds);
        } else {
            holder.tCardSounds.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }
}
