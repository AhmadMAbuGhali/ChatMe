package com.power.chatme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity {
    //Define the elements
    private Button sendNumber;
    private EditText phoneNumber;
    private CountryCodePicker ccp;
    public String ccpPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Connecting elements to the layout
        sendNumber = findViewById(R.id.sendNumber);
        phoneNumber = findViewById(R.id.phone_nu);
        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phoneNumber);

        // send user to check Code class
        sendNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumber.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Enter Phone Number ", Toast.LENGTH_SHORT).show();
                    return;
                }
                // get Full number with  country code
                ccpPhone = ccp.getFullNumberWithPlus().replace("", "");
                Intent intent = new Intent(RegisterActivity.this, CheckCodeActivity.class);
                // send Full Phone Number to Check code class
                intent.putExtra("FullPhone", ccpPhone);
                startActivity(intent);
                finish();


            }

        });

    }

}