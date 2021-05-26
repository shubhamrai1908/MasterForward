package com.shubham.masterapp;

public class Data {
    String id,sender,requested,operator,ground,message,time;

    public Data(String id, String sender, String requested, String operator, String ground, String message, String time) {
        this.id = id;
        this.sender = sender;
        this.requested = requested;
        this.operator = operator;
        this.ground = ground;
        this.message = message;
        this.time = time;
    }

    public Data() {
        this.id = "1";
        this.sender="+919340247090";
        this.requested="+917587193970";
        this.operator="AIRTEL";
        this.ground="----";
        this.message="JIO-+917587193970";
        this.time="11-05-2021: 11:22:33";
    }

    public String getid() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getRequested() {
        return requested;
    }

    public String getOperator() {
        return operator;
    }

    public String getGround() {
        return ground;
    }

    public String getMessage() {
        return message;
    }

    public String gettime() {
        return time;
    }
}
