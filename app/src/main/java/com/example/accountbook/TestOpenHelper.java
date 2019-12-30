package com.example.accountbook;

import android.content.ContentValues; /*SQLiteDatabaseにデータを追加するもの*/
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/*
https://qiita.com/kengo_kuwahara/items/a8ef858a9810cad42ca6
 */

public class TestOpenHelper extends SQLiteOpenHelper{

    //データベースの場0ジョン
    private static final int DATABASE_VERSION = 3;

    //データベース情報を変数に格納
    private static final String DATABASE_NAME = "TestDB.db";
    private static final String TABLE_NAME = "testdb";
    private static final String _ID = "_id";
    private static final String COLUMN_NAME_PURCHASE = "purchase";
    private static final String COLUMN_NAME_AMOUNT = "amount";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE" + TABLE_NAME + "(" +
                    _ID + " INTEGER PRIMARY KEY,"+
                    COLUMN_NAME_PURCHASE + " TEXT,"+
                    COLUMN_NAME_AMOUNT + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    TestOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        saveData(db, "コロッケ2個", 60 * 2);
        saveData(db, "頭痛薬", 1500);
        saveData(db, "美容院", 5000);
        saveData(db, "うどん", 100);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int onldVersion, int newVersion){
        db.execSQL(
                SQL_CREATE_ENTRIES
        );
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    public void saveData(SQLiteDatabase db, String purchace, int amount){
        ContentValues values = new ContentValues();
        values.put("Purchace", purchace);
        values.put("Amount", amount);
        db.insert("testdb", null, values);
    }

}
