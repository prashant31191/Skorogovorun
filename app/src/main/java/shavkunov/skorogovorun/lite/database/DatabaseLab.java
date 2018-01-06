package shavkunov.skorogovorun.lite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import shavkunov.skorogovorun.lite.database.SkorDBSchema.SkorTable.Cols;
import shavkunov.skorogovorun.lite.model.Card;

public class DatabaseLab {

    private static final String GET_CARDS_EXCEPTION = "getCardsException";
    private static final String GET_CARD_EXCEPTION = "getCardException";

    private volatile static DatabaseLab databaseLab;
    private SQLiteDatabase database;

    public static DatabaseLab getInstance(Context context) {
        if (databaseLab == null) {
            synchronized (DatabaseLab.class) {
                if (databaseLab == null) {
                    databaseLab = new DatabaseLab(context);
                }
            }
        }
        return databaseLab;
    }

    private DatabaseLab(Context context) {
        database = new SkorBaseHelper(context)
                .getWritableDatabase();
    }

    private static ContentValues getContentValues(Card card) {
        ContentValues values = new ContentValues();
        values.put(Cols.IMAGE, card.getImage());
        values.put(Cols.TITLE, card.getTitle());
        values.put(Cols.SOUNDS, card.getSounds());
        values.put(Cols.FAVORITE, card.isFavorite() ? 1 : 0);

        return values;
    }

    public void addCard(Card card) {
        ContentValues values = getContentValues(card);
        database.insert(SkorDBSchema.SkorTable.FAVORITE_TONGUE, null, values);
    }

    public void addCards(List<Card> cards) {
        for (Card c : cards) {
            c.setFavorite(true);
            addCard(c);
        }
    }

    public void deleteCard(String cardTitle) {
        database.delete(SkorDBSchema.SkorTable.FAVORITE_TONGUE,
                Cols.TITLE + " = ?", new String[]{cardTitle});
    }

    public void deleteCards() {
        database.delete(SkorDBSchema.SkorTable.FAVORITE_TONGUE, null, null);
    }

    private SkorCursorWrapper queryCards(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                SkorDBSchema.SkorTable.FAVORITE_TONGUE,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new SkorCursorWrapper(cursor);
    }

    public List<Card> getCards() {
        List<Card> cards = new ArrayList<>();
        SkorCursorWrapper cursor = queryCards(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cards.add(cursor.getCard());
                cursor.moveToNext();
            }
        } catch (SQLiteException e) {
            Log.e(GET_CARDS_EXCEPTION,
                    "The exception was caught in getCards() method in DatabaseLab.java");
        } finally {
            cursor.close();
        }

        return cards;
    }

    public Card getPatter(String title) {
        SkorCursorWrapper cursor = queryCards(Cols.TITLE + " = ?", new String[] {title});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCard();
        } catch (SQLiteException e) {
            Log.e(GET_CARD_EXCEPTION,
                    "The exception was caught in getCard() method in DatabaseLab.java");
        } finally {
            cursor.close();
        }

        return null;
    }
}
