package com.androiddeft.loginandregistration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_UNAME = "name";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LAST = "last_name";
    private static final String KEY_PHONE = "telephone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private EditText etTitle, etFullName, etLast, etPhone, etUsername, etPassword, etConfirmPassword;
    private String title, name, last_name, telephone, email, password, confirmPassword;
    private ProgressDialog pDialog;
    private String register_url = "http://119.59.103.121/app_mobile/crm_users/register.php";
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());
        setContentView(R.layout.activity_register);

        etTitle = findViewById(R.id.etTitle);
        etFullName = findViewById(R.id.etFullName);
        etLast = findViewById(R.id.etLast);
        etPhone = findViewById(R.id.etPhone);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        Button login = findViewById(R.id.btnRegisterLogin);
        Button register = findViewById(R.id.btnRegister);

        //เปิดหน้าจอเข้าสู่ระบบเมื่อคลิกปุ่มเข้าสู่ระบบ
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ดึงข้อมูลที่ป้อนในข้อความแก้ไข
                title = etTitle.getText().toString().trim();
                name = etFullName.getText().toString().trim();
                last_name = etLast.getText().toString().trim();
                telephone = etPhone.getText().toString().trim();
                email = etUsername.getText().toString().toLowerCase().trim();
                password = etPassword.getText().toString().trim();
                confirmPassword = etConfirmPassword.getText().toString().trim();

                if (validateInputs()) {
                    registerUser();
                }

            }
        });

    }

    //แสดงแถบความคืบหน้าขณะลงทะเบียน
    private void displayLoader() {
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("สมัครสมาชิก กรุณารอสักครู่ ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    //เรียกใช้กิจกรรมแดชบอร์ดเมื่อลงชื่อสมัครใช้สำเร็จ
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);
        finish();
    }

    private void registerUser() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
//            เติมพารามิเตอร์คำขอ
            request.put(KEY_TITLE, title);
            request.put(KEY_UNAME, name);
            request.put(KEY_LAST, last_name);
            request.put(KEY_PHONE, telephone);
            request.put(KEY_EMAIL, email);
            request.put(KEY_PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, register_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            // ตรวจสอบว่าผู้ใช้ลงทะเบียนสำเร็จหรือไม่
                            if (response.getInt(KEY_STATUS) == 0) {
                                //ตั้งค่าเซสชันผู้ใช้
                                session.loginUser( name,email);

                                loadDashboard();

                            } else if (response.getInt(KEY_STATUS) == 1) {
                                //แสดงข้อความแสดงข้อผิดพลาดหากชื่อผู้ใช้นั้นมีอยู่แล้ว
                                etUsername.setError("ชื่อผู้ใช้ที่ได้รับไปแล้ว!");
                                etUsername.requestFocus();

                            } else {
                                Toast.makeText(getApplicationContext(),
                                        response.getString(KEY_MESSAGE), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();

                        //แสดงข้อความผิดพลาดเมื่อใดก็ตามที่เกิดข้อผิดพลาด
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // เข้าถึง RequestQueue ผ่านคลาสซิงเกิลของคุณ
        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    //    ตรวจสอบอินพุตและแสดงข้อผิดพลาดถ้ามี
    private boolean validateInputs() {

        if (KEY_EMPTY.equals(title)) {
            etTitle.setError("กรูณากรอกคำนำหน้าชื่อ");
            etTitle.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(name)) {
            etFullName.setError("กรูณากรอกชื่อ");
            etFullName.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(last_name)) {
            etLast.setError("กรูณากรอกนามสกุล");
            etLast.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(telephone)) {
            etPhone.setError("กรุณากรอกเบอร์โทรศัพท์มือถือ");
            etPhone.requestFocus();
            return false;
        }
        if (KEY_EMPTY.equals(password)) {
            etPassword.setError("กรุณากรอกรหัสผ่าน");
            etPassword.requestFocus();
            return false;
        }

        if (KEY_EMPTY.equals(confirmPassword)) {
            etConfirmPassword.setError("ยืนยันรหัสผ่าน");
            etConfirmPassword.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("รหัสผ่านและยืนยันรหัสผ่านไม่ตรงกัน");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }
}