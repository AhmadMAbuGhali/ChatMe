package com.power.chatme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    private Button SendNumber;
    private EditText PhoneNumber;
    private CountryCodePicker ccp;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    EditText CodeText ;
    private String phoneVerificationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SendNumber =findViewById(R.id.sendNumber);
        PhoneNumber =findViewById(R.id.phone_nu);
        ccp =findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(PhoneNumber);
        CodeText= findViewById(R.id.CodeText);
        mAuth =FirebaseAuth.getInstance();


        SendNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(PhoneNumber.getText().toString().trim())){
                    Toast.makeText(RegisterActivity.this, "Please Enter a Phone Number", Toast.LENGTH_SHORT).show();
                }else {
                        String getNu = ccp.getFullNumberWithPlus().replace("","");
                        Authentication(getNu);
                }
            }
        });

    }
    protected void onStart() {
        super.onStart();
        if(currentUser!=null){
            SendUserToMainActivity();

        }
    }

    private void SendUserToMainActivity() {

        Intent MainIntent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(MainIntent);
    }


    private void Authentication(String Number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(Number)       // Phone number to verify
                        .setTimeout((long) 60, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                Toast.makeText(RegisterActivity.this, "Confirmed", Toast.LENGTH_SHORT).show();
                                signInWithPhoneAuthCredential(phoneAuthCredential);

                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,@NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                phoneVerificationId = verificationId;

                                super.onCodeSent(verificationId, forceResendingToken);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(RegisterActivity.this, "Verification is failed ", Toast.LENGTH_SHORT).show();
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    public void verifyCode(View view) {

        String code = CodeText.getText().toString();

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            String phoneNumber = user.getPhoneNumber();

                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            intent.putExtra("phone",phoneNumber);
                            Toast.makeText(RegisterActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            }

                    }
                });
    }
}