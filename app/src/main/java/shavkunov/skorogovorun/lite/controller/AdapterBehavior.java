package shavkunov.skorogovorun.lite.controller;

import android.app.Activity;

import java.util.List;

import shavkunov.skorogovorun.lite.model.Card;

public interface AdapterBehavior {

    void favoriteButton(CardsAdapter.TongueHolder holder, List<Card> cards,
                        Activity activity, CardsAdapter adapter);
}
