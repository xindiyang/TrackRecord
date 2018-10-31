package com.example.codoon.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.codoon.myapplication.SportLines.SportActivity;
import com.example.codoon.myapplication.Util.MyView;

public class MainActivity extends AppCompatActivity {
    private MyView myView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        myView = findViewById(R.id.myView);
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SportActivity.class);
                startActivity(intent);
            }
        });
    }

}
