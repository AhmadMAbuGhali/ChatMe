package com.power.chatme;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    //Define the elements
    private Button sendNumber;
    private EditText phoneNumber;
    private CountryCodePicker ccp;
    public String ccpPhone;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Connecting elements to the layout
        sendNumber = findViewById(R.id.sendNumber);
        phoneNumber = findViewById(R.id.phone_nu);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneNumber);
        mAuth = FirebaseAuth.getInstance();

        // send user to check Code class
        sendNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Phone Number ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    // get Full number with  country code
                    ccpPhone = ccp.getFullNumberWithPlus().replace("", "");
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(ccpPhone)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(RegisterActivity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }


                Intent intent = new Intent(RegisterActivity.this, CheckCodeActivity.class);
                // send Full Phone Number to Check code class
                intent.putExtra("FullPhone", ccpPhone);
                startActivity(intent);
                finish();


            }

        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent checkCodeIntint = new Intent(RegisterActivity.this, CheckCodeActivity.class);
                        checkCodeIntint.putExtra("auth", s);
                        startActivity(checkCodeIntint);
                    }
                }, 10000);
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            sendToMainActivity();
        }
    }

    private void sendToMainActivity() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void signIn(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendToMainActivity();
                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}