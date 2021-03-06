package shavkunov.skorogovorun.lite.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import shavkunov.skorogovorun.lite.database.SkorDBSchema.SkorTable.Cols;
import shavkunov.skorogovorun.lite.model.Card;

public class SkorCursorWrapper extends CursorWrapper {
    public SkorCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Card getCard() {
        String image = getString(getColumnIndex(Cols.IMAGE));
        String title = getString(getColumnIndex(Cols.TITLE));
        String sounds = getString(getColumnIndex(Cols.SOUNDS));
        int isFavorite = getInt(getColumnIndex(Cols.FAVORITE));

        Card card = new Card();
        card.setImage(image);
        card.setTitle(title);
        card.setSounds(sounds);
        card.setFavorite(isFavorite != 0);

        return card;
    }
}
