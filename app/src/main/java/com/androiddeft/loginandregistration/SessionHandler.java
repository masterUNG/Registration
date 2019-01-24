package com.androiddeft.loginandregistration;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UNAME = "name";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LAST = "last_name";
    private static final String KEY_PHONE = "telephone";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }



    //      ตรวจสอบว่าผู้ใช้เข้าสู่ระบบ
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* หากการตั้งค่าที่ใช้ร่วมกันไม่ได้มีค่า
         จากนั้นผู้ใช้ไม่ได้เข้าสู่ระบบ
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* ตรวจสอบว่าเซสชั่นหมดอายุโดยการเปรียบเทียบ
       วันที่ปัจจุบันและวันหมดอายุของเซสชัน
        */
        return currentDate.before(expiryDate);
    }

    /**
     * ดึงและส่งกลับรายละเอียดผู้ใช้
     *
     * @return user details
     */
    public User getUserDetails() {
        //ตรวจสอบว่าผู้ใช้เข้าสู่ระบบก่อน
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setTitle (mPreferences.getString(KEY_TITLE, KEY_EMPTY));
        user.setFullName (mPreferences.getString(KEY_UNAME, KEY_EMPTY));
        user.setLast_name (mPreferences.getString(KEY_LAST, KEY_EMPTY));
        user.setTelephone (mPreferences.getString(KEY_PHONE, KEY_EMPTY));
        user.setUsername (mPreferences.getString(KEY_EMAIL, KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }

    /**
     * ออกจากระบบผู้ใช้โดยการล้างเซสชั่น
     */
    public void logoutUser() {
        mEditor.clear();
        mEditor.commit();
    }

    //    บันทึกผู้ใช้โดยบันทึกรายละเอียดผู้ใช้และตั้งค่าเซสชัน
    public void loginUser(String email, String name) {
//, String title, String last_name, String telephone
        mEditor.putString(KEY_EMAIL, email);
        mEditor.putString(KEY_UNAME, name);
//        mEditor.putString(KEY_TITLE, title);
//        mEditor.putString(KEY_LAST, last_name);
//        mEditor.putString(KEY_PHONE, telephone);

        Date date = new Date();

        //ตั้งค่าเซสชันผู้ใช้สำหรับ 7 วันถัดไป
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.commit();
    }

}
