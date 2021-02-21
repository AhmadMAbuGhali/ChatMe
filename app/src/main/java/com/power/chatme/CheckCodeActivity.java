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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class CheckCodeActivity extends AppCompatActivity {
    //Define the elements
    private EditText codeText1, codeText2, codeText3, codeText4, codeText5, codeText6;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String FullPhone;
    String mVerificationId;
    long futureTimestamp = System.currentTimeMillis() + (60 * 1000);
    private Button verifyCode;

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

        //receive Full Phone Number From Register class
        FullPhone = getIntent().getStringExtra("FullPhone");
        // check that Number is in the correct Form
        Toast.makeText(CheckCodeActivity.this, "" + FullPhone, Toast.LENGTH_SHORT).show();
        //send verification code to Phone Number
        sendVerificationCode(FullPhone);
        // prepare Edit text
        setupCodeText();
        //setup timer

        TimerTextView msgTimerText = (TimerTextView) findViewById(R.id.msgTimerText);
        TimerTextView callTimerText = (TimerTextView) findViewById(R.id.callTimerText);
        msgTimerText.setEndTime(futureTimestamp);
        callTimerText.setEndTime(futureTimestamp);

        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                         if(codeText1.getText().toString().trim().isEmpty()
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
                                         codeText6.getText().toString() ;
                         if (mVerificationId !=null){
                             PhoneAuthCredential phoneAuthCredential =PhoneAuthProvider.getCredential(
                                     mVerificationId,code
                             );
                             FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                     .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                         @Override
                                         public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Intent intent = new Intent(CheckCodeActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Toast.makeText(CheckCodeActivity.this, "Invalid Code ", Toast.LENGTH_SHORT).show();
                                            }
                                         }
                                     });
                         }
            }
        });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {

                        VerificationCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(CheckCodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    mVerificationId = s;
                }
            };

    private void VerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        singInWithPhoneAuthCredential(credential);
    }

    private void singInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(CheckCodeActivity.this, "Confirm", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CheckCodeActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(CheckCodeActivity.this, "Not Confirm", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(CheckCodeActivity.this, "Try later", Toast.LENGTH_SHORT).show();

                            }
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


}