package com.shubham.masterapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG =
            MyReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";
    MasterDatabase masterDatabase;
    SmsManager smsManager;
    SubscriptionInfo  sim;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.
        SubscriptionManager localSubscriptionManager = SubscriptionManager.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
        sim=(SubscriptionInfo)localList.get(0);
        final SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        int pos=preferences.getInt("SelectedSim", 0);
        if(pos==0) {
            sim = (SubscriptionInfo) localList.get(0);
            //Toast.makeText(context,"sim1",Toast.LENGTH_SHORT).show();
        }
        if (pos==1)
        {
            sim=(SubscriptionInfo)localList.get(1);
           // Toast.makeText(context,"sim2",Toast.LENGTH_SHORT).show();

        }



        masterDatabase = new MasterDatabase(context);
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
        DateFormat f=new SimpleDateFormat();
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(time);
        final String myTime=f.format(calendar.getTime());
        mainMethod(sender,strMessage,context,myTime);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void mainMethod(String sender, String message, Context context, String time) {
        switch (verifySender(sender,message))
        {
            case 1:
                String reqno = message.substring(2, 12);
                String opname = message.substring(13).trim();
                insertIntoDB("gnd_to_master_table",sender,reqno,opname,"---",message,time);
                sendReqToServer(sender,reqno,opname);

                break;
             case 2:

                break;
            default:
                break;

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void sendReqToServer(String gnd, String reqno, String opname) {
        Date mytime= Calendar.getInstance().getTime();
        String server="+919399069424";
        String msg="";
        switch (opname.toLowerCase())
        {
            case "bsnl":
               // server="17254";
                msg="LOC "+reqno.trim();
                break;
            case "jio":
               // server="+917021265165";
                msg="91"+reqno.trim();
                break;
            case "airtel":
                //server="+918800112112";
                msg="91"+reqno.trim();
                break;
            case "idea":
                //server="+919713018509";
                msg="CEL 91"+reqno.trim();
                break;
            default:
                return;
        }
        SmsManager.getSmsManagerForSubscriptionId(sim.getSubscriptionId()).sendTextMessage(server, null,msg  , null, null);
        insertIntoDB("master_to_server_table",server,reqno,opname.trim(),gnd,msg,mytime.toString().trim());
        insertIntoDB("request_table",gnd,reqno,opname.trim(),"","",mytime.toString().trim());


    }

    private void insertIntoDB(String TableName, String sender, String reqno, String opname,String gnd, String message, String time) {
        switch(TableName)
        {
            case "gnd_to_master_table":
                masterDatabase.groundTOmaster(sender,reqno,opname,message,time);
                break;
            case "master_to_server_table":
                masterDatabase.masterTOserver(sender,reqno,opname,gnd,message,time);
                break;
            case "server_to_master_table":
                masterDatabase.serverTOmaster(sender,reqno,opname,message,time);
                break;
            case "master_to_gnd_table":
                masterDatabase.masterTOground(sender,reqno,opname,message,time);
                break;
            case "request_table":
                masterDatabase.requestTable(sender,reqno,opname,time);
                break;


        }

    }

    private int verifySender(String sender, String message) {
        //for ground sender
        if (sender.length() >= 10) {
            Cursor cursor = masterDatabase.getAll("groundlist_table");
            List<String> groundlist=new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    groundlist.add(cursor.getString(2));
                } while (cursor.moveToNext());
            }
            for(int i=0;i<groundlist.size();i++)
            {
                if(sender.contains(groundlist.get(i)))
                {
                    if(message.length()>=16&&message.length()<=18)
                        return 1;
                }
            }
        }
        //ground ends
        //for server

        return 0;
    }

}