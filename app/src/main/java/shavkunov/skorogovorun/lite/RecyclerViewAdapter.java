package shavkunov.skorogovorun.lite;

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
import shavkunov.skorogovorun.lite.model.DatabaseLab;
import shavkunov.skorogovorun.lite.model.Patter;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TongueHolder> {

    private List<Patter> patters;
    private Activity activity;
    private boolean isTongueTwistersActivity;

    public RecyclerViewAdapter(Activity activity, List<Patter> patters,
                               boolean isTongueTwistersActivity) {
        this.activity = activity;
        this.patters = patters;
        this.isTongueTwistersActivity = isTongueTwistersActivity;
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
        if (patters.get(position).getImage() != null) {
            holder.tCardImage.setVisibility(View.VISIBLE);
            Glide.with(activity)
                    .load(patters.get(position).getImage())
                    .apply(new RequestOptions()
                            .error(R.drawable.error)
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.tCardImage);
        } else {
            holder.tCardImage.setVisibility(View.GONE);
        }

        holder.tCardTitle.setText(patters.get(position).getTitle());

        holder.tCardFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.tCardFavoriteButton.isChecked()) {
                    if (patters.size() > 0) {
                        DatabaseLab.get(activity).addPatter(new Patter(
                                patters.get(holder.getAdapterPosition()).getImage(),
                                patters.get(holder.getAdapterPosition()).getTitle(),
                                patters.get(holder.getAdapterPosition()).getSounds(),
                                patters.get(holder.getAdapterPosition()).isFavorite()
                        ));
                    }
                } else {
                    DatabaseLab.get(activity)
                            .deletePatter(patters.get(holder.getAdapterPosition()).getTitle());

                    if (!isTongueTwistersActivity) {
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                }
            }
        });

        if (patters.get(position).getSounds() != null) {
            String sounds = activity.getString(R.string.letters) + " " +
                    patters.get(position).getSounds();
            holder.tCardSounds.setText(sounds);
        } else {
            holder.tCardSounds.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return patters.size();
    }
}
