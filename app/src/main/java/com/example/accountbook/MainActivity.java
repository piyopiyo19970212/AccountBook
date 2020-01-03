package com.example.accountbook;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TestOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);


        EditText purchaseText = findViewById(R.id.input_purchase);
        EditText priceText = findViewById(R.id.input_price);
        priceText.setInputType(InputType.TYPE_CLASS_NUMBER); //数字だけ入力させる

        /* 日付の入力 */
        EditText dateText = findViewById(R.id.input_date);
        dateText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                dateText.setText(String.format("%d / %02d / %02d", year, month+1, dayOfMonth));
                            }
                        },
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DATE)
                );
                datePickerDialog.show();
            }
        });

        /* 追加ボタン */
        Button register = findViewById(R.id.add);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String purchase = purchaseText.getText().toString();
                String price = priceText.getText().toString();
                String date = dateText.getText().toString();
                if(purchase.equals("") || price.equals("") || date.equals("")){
                    Toast.makeText(MainActivity.this, "入力されていない項目があります", Toast.LENGTH_LONG).show();
                }
                else { //登録作業
                    if(helper == null) { helper = new TestOpenHelper(getApplicationContext()); }
                    SQLiteDatabase db = helper.getWritableDatabase();
                    insertData(db, date, purchase, Integer.valueOf(price));
                    Toast.makeText(MainActivity.this, price+"円の"+purchase+"を登録しました", Toast.LENGTH_LONG).show();
                }
            }
        });

        /* 更新ボタン */
        Button update = findViewById(R.id.update);
        update.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String purchase = purchaseText.getText().toString();
                String price = priceText.getText().toString();
                if(purchase.equals("") || price.equals("")){
                    Toast.makeText(MainActivity.this, "入力されていない項目があります", Toast.LENGTH_LONG).show();
                }
                else { //更新作業
                    if(helper == null) { helper = new TestOpenHelper(getApplicationContext()); }
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues val = new ContentValues();
                    val.put("price", Integer.parseInt(price));
                    db.update("testdb", val, "purchase=?", new String[]{purchase});
                    Toast.makeText(MainActivity.this, "更新しました", Toast.LENGTH_LONG).show();
                }
            }
        });

        /* 削除ボタン */
        Button delete = findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String purchase = purchaseText.getText().toString();
                String price = priceText.getText().toString();
                if(purchase.equals("") && price.equals("")){
                    Toast.makeText(MainActivity.this, "入力されていない項目があります", Toast.LENGTH_LONG).show();
                }
                else {
                    if(helper == null) { helper = new TestOpenHelper(getApplicationContext()); }
                    SQLiteDatabase db = helper.getWritableDatabase();
                    String sql = "delete from testdb where (purchase=\'"+purchase+"\' and price=\'"+price+"\');";
                    db.execSQL(sql);
                    Toast.makeText(MainActivity.this, "削除しました", Toast.LENGTH_LONG).show();
                }
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
                new String[] { "date", "purchase", "price" },
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
            sbuilder.append("    ");
            sbuilder.append(cursor.getString(1));
            sbuilder.append(":    ");
            sbuilder.append(cursor.getInt(2));
            sbuilder.append("円\n");
            cursor.moveToNext();
        }

        cursor.close();

        textView.setText(sbuilder.toString());
    }

    private void insertData(SQLiteDatabase db, String date, String purchase, int price) {
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("purchase", purchase);
        values.put("price", price);
        db.insert("testdb", null, values);
    }
}
