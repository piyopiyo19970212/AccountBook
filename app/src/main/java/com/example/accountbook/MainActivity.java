package com.example.accountbook;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TestOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = findViewById(R.id.text_view);


        /* ツールバー */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        EditText purchaseText = findViewById(R.id.input_purchase);
        EditText priceText = findViewById(R.id.input_price);
        priceText.setInputType(InputType.TYPE_CLASS_NUMBER); //数字だけ入力させる

        /* 登録ボタン */
        Button register = findViewById(R.id.registration);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String purchase = purchaseText.getText().toString();
                String price = priceText.getText().toString();
                if(purchase.equals("") || price.equals("")){
                    Toast.makeText(MainActivity.this, "入力されていない項目があります", Toast.LENGTH_LONG).show();
                }
                else { //登録作業
                    if(helper == null) { helper = new TestOpenHelper(getApplicationContext()); }
                    SQLiteDatabase db = helper.getWritableDatabase();
                    insertData(db, purchase, Integer.valueOf(price));
                    Toast.makeText(MainActivity.this, "登録しました", Toast.LENGTH_LONG).show();
                }
            }
        });

        /* 削除ボタン */
        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "削除しました", Toast.LENGTH_LONG).show();
            }
        });

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
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                "testdb",
                new String[] { "purchase", "price" },
                null,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();

        StringBuilder sbuilder = new StringBuilder();

        for (int i = 0; i < cursor.getCount(); i++) {
            sbuilder.append(cursor.getString(0));
            sbuilder.append(":    ");
            sbuilder.append(cursor.getInt(1));
            sbuilder.append("円\n\n");
            cursor.moveToNext();
        }

        cursor.close();

        textView.setText(sbuilder.toString());
    }

    private void insertData(SQLiteDatabase db, String purchase, int price) {
        ContentValues values = new ContentValues();
        values.put("purchase", purchase);
        values.put("price", price);
    }
}
