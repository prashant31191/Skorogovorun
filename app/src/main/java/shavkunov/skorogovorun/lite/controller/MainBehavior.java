package shavkunov.skorogovorun.lite.controller;

import android.app.Activity;
import android.view.View;

import java.util.List;

import shavkunov.skorogovorun.lite.database.DatabaseLab;
import shavkunov.skorogovorun.lite.model.Card;

public class MainBehavior implements AdapterBehavior {

    @Override
    public void favoriteButton(final CardsAdapter.TongueHolder holder, final List<Card> cards,
                               final Activity activity, CardsAdapter adapter) {
        holder.tCardFavoriteButton.setChecked(cards.get(holder.getAdapterPosition()).isFavorite());

        Card patter = DatabaseLab.getInstance(activity)
                .getPatter(cards.get(holder.getAdapterPosition()).getTitle());

        if (patter != null) {
            cards.get(holder.getAdapterPosition()).setFavorite(patter.isFavorite());
            holder.tCardFavoriteButton.setChecked(patter.isFavorite());
        }

        holder.tCardFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.tCardFavoriteButton.isChecked()) {
                    DatabaseLab.getInstance(activity).addCard(new Card(
                            cards.get(holder.getAdapterPosition()).getImage(),
                            cards.get(holder.getAdapterPosition()).getTitle(),
                            cards.get(holder.getAdapterPosition()).getSounds(),
                            true));
                } else {
                    DatabaseLab.getInstance(activity)
                            .deleteCard(cards.get(holder.getAdapterPosition()).getTitle());

                    cards.get(holder.getAdapterPosition()).setFavorite(false);
                }
            }
        });
    }
}
