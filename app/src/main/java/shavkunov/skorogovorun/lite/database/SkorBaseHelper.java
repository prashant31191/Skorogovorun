package shavkunov.skorogovorun.lite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static shavkunov.skorogovorun.lite.database.SkorDBSchema.*;

public class SkorBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "skorBase.db";

    public SkorBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + SkorTable.FAVORITE_TONGUE + "(" +
        "_id integer primary key autoincrement, " +
        SkorTable.Cols.IMAGE + ", " +
        SkorTable.Cols.TITLE + ", " +
        SkorTable.Cols.SOUNDS + ", " +
        SkorTable.Cols.FAVORITE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
