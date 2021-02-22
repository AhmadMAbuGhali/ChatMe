package com.power.chatme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dhims.timerview.TimerTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class CheckCodeActivity extends AppCompatActivity {
    //Define the elements
    private EditText codeText1, codeText2, codeText3, codeText4, codeText5, codeText6;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String FullPhone;
    String mVerificationId;
    long futureTimestamp = System.currentTimeMillis() + (60 * 1000);
    private Button verifyCode;
    private String OPT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);
        //Connecting elements to the layout
        codeText1 = findViewById(R.id.codeText1);
        codeText2 = findViewById(R.id.codeText2);
        codeText3 = findViewById(R.id.codeText3);
        codeText4 = findViewById(R.id.codeText4);
        codeText5 = findViewById(R.id.codeText5);
        codeText6 = findViewById(R.id.codeText6);
        verifyCode = findViewById(R.id.verifyCode);
        mAuth = FirebaseAuth.getInstance();
        FullPhone = getIntent().getStringExtra("FullPhone");
        OPT = getIntent().getStringExtra("auth");

        Toast.makeText(CheckCodeActivity.this, "" + FullPhone, Toast.LENGTH_SHORT).show();
        // prepare Edit text
        setupCodeText();
        //setup timer
        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeText1.getText().toString().trim().isEmpty()
                        || codeText2.getText().toString().trim().isEmpty()
                        || codeText3.getText().toString().trim().isEmpty()
                        || codeText4.getText().toString().trim().isEmpty()
                        || codeText5.getText().toString().trim().isEmpty()
                        || codeText6.getText().toString().trim().isEmpty()
                ) {
                    Toast.makeText(CheckCodeActivity.this, "Please Enter valid Code", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code =
                        codeText1.getText().toString() +
                                codeText2.getText().toString() +
                                codeText3.getText().toString() +
                                codeText4.getText().toString() +
                                codeText5.getText().toString() +
                                codeText6.getText().toString();
                if (OPT != null) {
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(OPT, code);
                    singIn(phoneAuthCredential);
                }
            }
        });

        TimerTextView msgTimerText = (TimerTextView) findViewById(R.id.msgTimerText);
        TimerTextView callTimerText = (TimerTextView) findViewById(R.id.callTimerText);
        msgTimerText.setEndTime(futureTimestamp);
        callTimerText.setEndTime(futureTimestamp);

    }

    private void singIn(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendToMain();
                } else {
                    Toast.makeText(CheckCodeActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void setupCodeText() {
        codeText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    codeText2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        codeText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    codeText3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        codeText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    codeText4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        codeText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    codeText5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        codeText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    codeText6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        startActivity(new Intent(CheckCodeActivity.this, MainActivity.class));
        finish();
    }

}