package com.example.accountbook;

import android.app.DatePickerDialog;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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

public class Delete extends AppCompatActivity {

    private TestOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete);

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
                        Delete.this,
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

        /* 削除作業 */
        Button add = findViewById(R.id.delete);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contents = contentsText.getText().toString();
                String price = priceText.getText().toString();
                String date = dateText.getText().toString();
                RadioButton radio = findViewById(rg.getCheckedRadioButtonId());
                String ie = radio.getText().toString();
                if(contents.equals("") || price.equals("") || date.equals("")){
                    Toast.makeText(Delete.this, "入力されていない項目があります", Toast.LENGTH_LONG).show();
                }
                else { //削除作業
                    if(helper == null) { helper = new TestOpenHelper(getApplicationContext()); }
                    db = helper.getWritableDatabase();
                    deleteData(db, ie, date, contents, Integer.valueOf(price));
                }
            }
        });
    }

    private void deleteData(SQLiteDatabase db, String ie, String date, String contents, int price) {
        String sql = "delete from testdb where (" +
                "ie=\'"+ie+"\' and " +
                "date=\'"+date+"\' and " +
                "contents=\'"+contents+"\' and " +
                "price=\'"+price+"\');";
        System.out.println(sql);
        try {
            db.execSQL(sql);
            Toast.makeText(Delete.this, "該当データを削除しました", Toast.LENGTH_LONG).show();
        }catch (SQLException e){
            Toast.makeText(Delete.this, "該当データはありません", Toast.LENGTH_LONG).show();
        }
    }
}
