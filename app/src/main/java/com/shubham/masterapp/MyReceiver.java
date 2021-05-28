package com.shubham.masterapp;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG =
            MyReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    MasterDatabase dataBaseHelper;
    SmsManager smsManager = SmsManager.getDefault();

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.
        dataBaseHelper = new MasterDatabase(context);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String sender="";
        long time = 0;
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
                sender=msgs[i].getOriginatingAddress();
                time=msgs[i].getTimestampMillis();
                strMessage +=msgs[i].getMessageBody() + "\n";
                // Log and display the SMS message.
                Log.d(TAG, "onReceive: " + strMessage);
                //Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
            }
        }
        addMessageToCloud(sender,strMessage,context,time);

    }

    private void addMessageToCloud(String sender, String strMessage, Context context, long time) {
        DateFormat format=new SimpleDateFormat();
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        final String myTime=format.format(calendar.getTime());

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Ground");
        }
    }
}