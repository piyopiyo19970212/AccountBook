package com.example.accountbook;

import android.content.ContentValues; /*SQLiteDatabaseにデータを追加するもの*/
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/*
https://qiita.com/kengo_kuwahara/items/a8ef858a9810cad42ca6
 */

public class TestOpenHelper extends SQLiteOpenHelper{

    //データベースのバージョン
    private static final int DATABASE_VERSION = 3;

    //データベース情報を変数に格納
    private static final String DATABASE_NAME = "TestDB.db";
    private static final String TABLE_NAME = "testdb";
    private static final String _ID = "_id";
    private static final String COLUMN_NAME_TIME = "time";
    private static final String COLUMN_NAME_PURCHASE = "purchase";
    private static final String COLUMN_NAME_PRICE = "price";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY,"+
//                    COLUMN_NAME_TIME + " TIME,"+
                    COLUMN_NAME_PURCHASE + " TEXT,"+ COLUMN_NAME_PRICE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    TestOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //テーブル作成
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    // アップデートの判別，古いバージョンは削除して新規作成する
    @Override
    public void onUpgrade(SQLiteDatabase db, int onldVersion, int newVersion){
        db.execSQL(
                SQL_DELETE_ENTRIES
        );
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
