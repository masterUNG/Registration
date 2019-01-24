package com.androiddeft.loginandregistration;

import java.util.Date;

public class User {
    String title;
    String name;
    String last_name;
    String telephone;
    String email;
//    เซสชั่นวันหมดอายุ
    Date sessionExpiryDate;

    //    ส่วนของการกรอกข้อมุลในช่อง register สำหรับการติดตั้ง
    public void setTitle(String title) {
        this.title = title;
    }

    public void setFullName(String name) {
        this.name = name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setUsername(String email) {
        this.email = email;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    //    ประกาศ สติงอีกที สำหรับการรับค่า
    public String getTitle() {
        return title;
    }

    public String getFullName () {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getUsername() {
        return email;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
}
