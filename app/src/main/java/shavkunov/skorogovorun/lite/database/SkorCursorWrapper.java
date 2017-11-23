package shavkunov.skorogovorun.lite.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import shavkunov.skorogovorun.lite.database.SkorDBSchema.SkorTable.Cols;
import shavkunov.skorogovorun.lite.model.Patter;

public class SkorCursorWrapper extends CursorWrapper {
    public SkorCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Patter getPatter() {
        String image = getString(getColumnIndex(Cols.IMAGE));
        String title = getString(getColumnIndex(Cols.TITLE));
        String sounds = getString(getColumnIndex(Cols.SOUNDS));
        int isFavorite = getInt(getColumnIndex(Cols.FAVORITE));

        Patter patter = new Patter();
        patter.setImage(image);
        patter.setTitle(title);
        patter.setSounds(sounds);
        patter.setFavorite(isFavorite != 0);

        return patter;
    }
}
