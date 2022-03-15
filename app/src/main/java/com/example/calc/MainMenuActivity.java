package com.example.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void openSimpleCalcBtn(View view) {
        Intent intent = new Intent(this, SimpleCalcActivity.class);
        startActivity(intent);
    }

    public void openAboutBtn(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}