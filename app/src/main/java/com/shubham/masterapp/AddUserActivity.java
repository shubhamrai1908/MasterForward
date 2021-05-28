package com.shubham.masterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUserActivity extends AppCompatActivity {
    EditText et1,et2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        et1=findViewById(R.id.name);
        et2=findViewById(R.id.phone);
        button=findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=et1.getText().toString().trim();
                String phone=et2.getText().toString().trim();
                if(name.isEmpty())
                {
                    et1.setError("Enter a valid name");
                }
                if(phone.isEmpty()||phone.length()<10)
                {
                    et2.setError("Enter a valid mobile");
                    et2.requestFocus();
                    return;
                }
                try {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("ground");
                    Ground ground=new Ground(name,phone);
                    String groundId = mDatabase.push().getKey();
                    mDatabase.child(groundId).setValue(ground).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddUserActivity.this, "Added", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(AddUserActivity.this,"Not Added!!!",Toast.LENGTH_LONG).show();
                        }
                    });
                }catch (Exception e)
                {
                    Toast.makeText(AddUserActivity.this,e.toString(),Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}