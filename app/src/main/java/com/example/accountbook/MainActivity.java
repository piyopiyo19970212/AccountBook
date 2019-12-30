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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;

import static android.widget.Toast.*;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        /* 登録ボタン */
        Button register = findViewById(R.id.registration);
        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText purchaseText = findViewById(R.id.input_purchase);
                String purchase = purchaseText.getText().toString();

                EditText amountText = findViewById(R.id.input_amount);
                amountText.setInputType(InputType.TYPE_CLASS_NUMBER); //数字だけ入力させる
                String amount = amountText.getText().toString();
                if(purchase.equals("") || amount.equals("")){
                    Toast.makeText(MainActivity.this, "入力されていない項目があります", Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(MainActivity.this, "登録しました", Toast.LENGTH_LONG).show();
            }
        });
    }
}
