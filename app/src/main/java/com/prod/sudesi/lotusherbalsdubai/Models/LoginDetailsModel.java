package com.prod.sudesi.lotusherbalsdubai.Models;

/**
 * Created by Admin on 25-10-2017.
 */

public class LoginDetailsModel {

    String username;
    String password;
    String android_uid;
    String created_date;
    String last_modified_date;
    String ba_name;
    String Attendance;

    public String getAttendance() {
        return Attendance;
    }

    public void setAttendance(String attendance) {
        Attendance = attendance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAndroid_uid() {
        return android_uid;
    }

    public void setAndroid_uid(String android_uid) {
        this.android_uid = android_uid;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(String last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public String getBa_name() {
        return ba_name;
    }

    public void setBa_name(String ba_name) {
        this.ba_name = ba_name;
    }


}
