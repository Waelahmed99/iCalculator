package idev.mysimplecalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class EquationsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Equations.db";

    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "equations";
        public static final String COLUMN_ID = "ID";
        public static final String COLUMN_EQUATION = "equations";
        public static final String COLUMN_TIME = "time";
    }

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry.COLUMN_ID  + " INTEGER PRIMARY KEY, " +
                    FeedEntry.COLUMN_EQUATION + " TEXT," +
                    FeedEntry.COLUMN_TIME + " TEXT)";

    public EquationsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    public Cursor viewData() {
        return this.getReadableDatabase().rawQuery("SELECT * FROM " + FeedEntry.TABLE_NAME, null);
    }

    public void deleteData() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + FeedEntry.TABLE_NAME);
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(FeedEntry.TABLE_NAME, "ID = " + id, null);
    }

    public void restoreItem(EquationsModel deletedItem, int deletedIndex) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_ID, deletedIndex);
        values.put(FeedEntry.COLUMN_EQUATION, deletedItem.equation);
        values.put(FeedEntry.COLUMN_TIME, deletedItem.date);
        db.insert(FeedEntry.TABLE_NAME, null, values);
    }
}
