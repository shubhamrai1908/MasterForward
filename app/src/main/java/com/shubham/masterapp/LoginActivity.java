package com.shubham.masterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class LoginActivity extends AppCompatActivity {
    EditText editText,editText1;
    Button button,button1;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText = findViewById(R.id.editText);
        editText1 = findViewById(R.id.editText1);
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=editText.getText().toString().trim();
                String passcode=editText1.getText().toString().trim();
                if(phone.length()==0)
                {
                    editText.setError("Enter a valid email");
                    return;
                }
                if(passcode.length()!=6)
                {
                    editText1.setError("Enter valid passcode");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,CreateActivity.class);
                startActivity(intent);

            }
        });


    }
}