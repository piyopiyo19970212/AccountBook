package com.example.accountbook;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Update extends AppCompatActivity {

    private TestOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);

        EditText contentsText = findViewById(R.id.input_contents);
        EditText priceText = findViewById(R.id.input_price);
        priceText.setInputType(InputType.TYPE_CLASS_NUMBER); //数字だけ入力させる
        RadioGroup rg = findViewById(R.id.io);

        /* 日付の入力 */
        EditText dateText = findViewById(R.id.input_date);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Update.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                dateText.setText(String.format("%d / %02d / %02d", year, month + 1, dayOfMonth));
                            }
                        },
                        date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE)
                );
                datePickerDialog.show();
            }
        });

        /* 登録作業 */
        Button add = findViewById(R.id.update);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contents = contentsText.getText().toString();
                String price = priceText.getText().toString();
                String date = dateText.getText().toString();
                RadioButton radio = findViewById(rg.getCheckedRadioButtonId());
                String ie = radio.getText().toString();
                if(contents.equals("") || price.equals("") || date.equals("")){
                    Toast.makeText(Update.this, "入力されていない項目があります", Toast.LENGTH_LONG).show();
                }
                else { //登録作業
                    if(helper == null) { helper = new TestOpenHelper(getApplicationContext()); }
                    db = helper.getWritableDatabase();
                    updatetData(db, ie, date, contents, Integer.valueOf(price));
                }
            }
        });
    }

    private void updatetData(SQLiteDatabase db, String ie, String date, String contents, int price) {
        String sql = "update testdb set price=\'"+price+"\' where (ie=\'"+ie+"\' and date=\'"+date+"\' and contents=\'"+contents+"\');";
        try{
            db.execSQL(sql);
            Toast.makeText(Update.this, "値段を更新しました", Toast.LENGTH_LONG).show();
        }catch(SQLiteException e){
            Toast.makeText(Update.this, "該当するデータがありません", Toast.LENGTH_LONG).show();
        }


    }
}