package com.shubham.masterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class CreateActivity extends AppCompatActivity {
    EditText et1,et2,et3,et4;
    Button button;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        et1=findViewById(R.id.editText1);
        et2=findViewById(R.id.editText2);
        et3=findViewById(R.id.editText3);
        et4=findViewById(R.id.editText4);
        button=findViewById(R.id.button);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=et1.getText().toString().trim();
                String phone=et2.getText().toString().trim();
                String email=et3.getText().toString().trim();
                String passcode=et4.getText().toString().trim();
                if(name.length()==0)
                {
                    et1.setError("Enter a valid mobile number");
                    return;
                }
                if(phone.length()!=10)
                {
                    et2.setError("Enter a valid mobile number");
                    return;
                }
                if(email.length()==0)
                {
                    et3.setError("Enter a valid email");
                    return;
                }
                if(passcode.length()!=6)
                {
                    et4.setError("Enter valid passcode");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

            }
        });
    }
}