package shavkunov.skorogovorun.lite.controller;

import android.app.Activity;
import android.view.View;

import java.util.List;

import shavkunov.skorogovorun.lite.database.DatabaseLab;
import shavkunov.skorogovorun.lite.model.Card;

public class FavoriteBehavior implements AdapterBehavior {

    @Override
    public void favoriteButton(final CardsAdapter.TongueHolder holder, final List<Card> cards,
                               final Activity activity, final CardsAdapter adapter) {
        holder.tCardFavoriteButton.setChecked(cards.get(holder.getAdapterPosition()).isFavorite());

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

                    cards.remove(holder.getAdapterPosition());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
