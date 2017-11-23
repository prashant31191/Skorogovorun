package shavkunov.skorogovorun.lite.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import shavkunov.skorogovorun.lite.database.SkorBaseHelper;
import shavkunov.skorogovorun.lite.database.SkorCursorWrapper;
import shavkunov.skorogovorun.lite.database.SkorDBSchema;
import shavkunov.skorogovorun.lite.database.SkorDBSchema.SkorTable.Cols;

public class DatabaseLab {

    public static final String EXCEPTION = "SQLiteException";

    private static DatabaseLab databaseLab;
    private SQLiteDatabase database;
    private Context context;

    public static DatabaseLab get(Context context) {
        if (databaseLab == null) {
            databaseLab = new DatabaseLab(context);
        }
        return databaseLab;
    }

    private DatabaseLab(Context context) {
        this.context = context.getApplicationContext();
        database = new SkorBaseHelper(context)
                .getWritableDatabase();
    }

    private static ContentValues getContentValues(Patter patter) {
        ContentValues values = new ContentValues();
        values.put(Cols.IMAGE, patter.getImage());
        values.put(Cols.TITLE, patter.getTitle());
        values.put(Cols.SOUNDS, patter.getSounds());
        values.put(Cols.FAVORITE, patter.isFavorite() ? 1 : 0);

        return values;
    }

    private void addPatter(Patter patter) {
        ContentValues values = getContentValues(patter);
        database.insert(SkorDBSchema.SkorTable.FAVORITE_TONGUE, null, values);
    }

    private SkorCursorWrapper queryPatters(String whereClause, String[] whereArgs) {
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

    public List<Patter> getPatters() {
        List<Patter> patters = new ArrayList<>();
        SkorCursorWrapper cursor = queryPatters(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                patters.add(cursor.getPatter());
                cursor.moveToNext();
            }
        } catch (SQLiteException e) {
            Log.e(EXCEPTION,
                    "The exception was caught in getPatters() method in DatabaseLab.java");
        } finally {
            cursor.close();
        }

        return patters;
    }
}
