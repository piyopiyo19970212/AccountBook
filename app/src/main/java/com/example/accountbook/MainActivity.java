package com.example.accountbook;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TestOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        //readData();

        /* 追加ボタン */
        Button register = findViewById(R.id.add);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Add.class));
            }
        });

//        /* 更新ボタン */
//        Button update = findViewById(R.id.update);
//        update.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String date = dateText.getText().toString();
//                String purchase = purchaseText.getText().toString();
//                String price = priceText.getText().toString();
//                if(purchase.equals("") || price.equals("") || date.equals("")){
//                    Toast.makeText(MainActivity.this, "入力されていない項目があります", Toast.LENGTH_LONG).show();
//                }
//                else { //更新作業
//                    if(helper == null) { helper = new TestOpenHelper(getApplicationContext()); }
//                    SQLiteDatabase db = helper.getWritableDatabase();
//                    String sql = "update testdb set price=\'"+price+"\' where (date=\'"+date+"\' and purchase=\'"+purchase+"\');";
//                    db.execSQL(sql);
//                    Toast.makeText(MainActivity.this, "値段を更新しました", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
        /* 削除ボタン */
        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.example.accountbook.Delete.class));
            }
        });

        /* 全削除ボタン */
        Button delete_all = findViewById(R.id.delete_all);
        delete_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("データを全て削除してもよろしいですか？")
                        .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(helper == null) { helper = new TestOpenHelper(getApplicationContext()); }
                                SQLiteDatabase db = helper.getWritableDatabase();
                                String sql = "delete from testdb";
                                db.execSQL(sql);
                                Toast.makeText(MainActivity.this, "全削除しました", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //全削除をキャンセル
                            }
                        });
                // ダイアログの表示
                builder.create().show();
            }
        });
//
        /* 表示ボタン */
        Button display = findViewById(R.id.display);
        display.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "DBを表示します", Toast.LENGTH_LONG).show();
                readData();
            }
        });
    }

    private void readData() {
        if(helper == null) { helper = new TestOpenHelper(getApplicationContext()); }
        db = helper.getReadableDatabase();
        try {
            Cursor cursor = db.query(
                    "testdb",
                    new String[]{"ie", "date", "contents", "price"},
                    null,
                    null,
                    null,
                    null,
                    null
            );
            cursor.moveToFirst();

            StringBuilder sbuilder = new StringBuilder();

            for (int i = 0; i < cursor.getCount(); i++) {
                sbuilder.append(cursor.getString(1));
                sbuilder.append(":    ");
                sbuilder.append(cursor.getString(0));
                sbuilder.append("    ");
                sbuilder.append(cursor.getString(2));
                sbuilder.append("    ");
                sbuilder.append(cursor.getInt(3));
                sbuilder.append("円\n");
                cursor.moveToNext();
            }
            cursor.close();

            textView.setText(sbuilder.toString());
        }catch(SQLiteException e){
            Toast.makeText(MainActivity.this, "登録されているデータはありません", Toast.LENGTH_LONG).show();
        }
    }
}
