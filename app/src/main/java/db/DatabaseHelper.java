package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mp3player.durgesh.com.mp3player.bean.DataBaseRecFileModel;

import java.util.ArrayList;

/**
 * Created by ketanparmar on 20/01/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context ctx;
    private static final String TAG = "DatabaseHelper";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SiloCloud";

    // Table Names
    public static final String TABLE_HISTORY = "UploadHistory";

    // Common column names
    public static final String KEY_ID = "_id";

    // Routes Table - column names
    public static final String FILE_TITLE = "title";
    public static final String FILE_PATH = "filepath";
    public static final String FILE_TYPE = "filetype";

    // Table Create Statements
    // route table create statement
    private static final String CREATE_TABLE_ROUTE = "CREATE TABLE "
            + TABLE_HISTORY + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + FILE_TITLE + " TEXT,"
            + FILE_TYPE + " TEXT,"
            + FILE_PATH + " TEXT" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_ROUTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }


    public ArrayList<DataBaseRecFileModel> getAllRecentFiles() {
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            Cursor c = db.rawQuery("select * from " + TABLE_HISTORY + "  limit 13", null, null);
            ArrayList<DataBaseRecFileModel> arrToDos = new ArrayList<DataBaseRecFileModel>();
            if (c.moveToFirst()) {
                do {
                    DataBaseRecFileModel rm = new DataBaseRecFileModel();
                    rm.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                    rm.setTitle(c.getString(c.getColumnIndex(FILE_TITLE)));
                    rm.setFilePath(c.getString(c.getColumnIndex(FILE_PATH)));
                    rm.setFileType(c.getString(c.getColumnIndex(FILE_TYPE)));
                    arrToDos.add(rm);

                } while (c.moveToNext());
            }

            return arrToDos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {

        }
    }

    public void addFielToTable(DataBaseRecFileModel contact) {
        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(FILE_TITLE, contact.getTitle()); // File Name
            values.put(FILE_PATH, contact.getFilePath()); // File Path
            values.put(FILE_TYPE, contact.getFileType()); // File Path
            // Inserting Row
            db.insert(TABLE_HISTORY, null, values);
            //2nd argument is String containing nullColumnHack

        } catch (Exception e) {
            String a = "";
        }

    }

    public boolean reNameFile(String filePath, DataBaseRecFileModel bin) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            Cursor c = db.rawQuery("UPDATE " + TABLE_HISTORY + " SET title = ? WHERE filepath = ?", new String[]{filePath, bin.getFilePath()}, null);
            if (c.moveToFirst()) {

            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    public boolean deleteFile(String filePath) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            Cursor c = db.rawQuery("DELETE from " + TABLE_HISTORY + " WHERE filepath = ?", new String[]{filePath}, null);
            if (c.moveToFirst()) {

            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

}
