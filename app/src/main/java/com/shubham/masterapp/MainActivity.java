package com.shubham.masterapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String[] appPermissions={Manifest.permission.SEND_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.FOREGROUND_SERVICE};
    private static final int PERMISSION_REQUEST_CODE = 1240;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkAndRequestPermissions()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

    }


    public boolean checkAndRequestPermissions()
    {
// Check which permissions are granted
        List<String> listPermissionsNeeded =new ArrayList<>();
        for (String perm: appPermissions)
        {

            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED)
            {
                listPermissionsNeeded.add( perm);

            }
        }
// Ask for non-granted permissions
        if (!listPermissionsNeeded. isEmpty())
        {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded. toArray(new String[listPermissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
            return false;

        }

// App has all permissions. Proceed ahead

        return true;

    }
}