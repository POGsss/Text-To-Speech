package com.example.texttospeech;

import androidx.appcompat.app.AppCompatActivity;
import android.content.*;
import android.view.*;
import android.os.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, GetStartedActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}