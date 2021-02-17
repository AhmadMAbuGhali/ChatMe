package com.power.chatme;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.dhims.timerview.TimerTextView;

public class CheckCodeActivity extends AppCompatActivity {
  EditText CodeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);
        CodeText = findViewById(R.id.CodeText);


        long futureTimestamp = System.currentTimeMillis() + (1 * 60 * 1000);
        TimerTextView timerText = (TimerTextView) findViewById(R.id.timerText);
        timerText.setEndTime(futureTimestamp);
    }


}