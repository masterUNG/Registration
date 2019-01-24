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

public class LoginActivity extends AppCompatActivity {
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_UNAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private EditText etUsername, etPassword;
    private String email, password;
    private ProgressDialog pDialog;
    private String login_url = "http://119.59.103.121/app_mobile/crm_users/login.php";
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionHandler(getApplicationContext());

        if(session.isLoggedIn()){
            loadDashboard();
        }
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);

        Button register = findViewById(R.id.btnLoginRegister);
        Button login = findViewById(R.id.btnLogin);

        //เปิดหน้าจอการลงทะเบียนเมื่อคลิกปุ่มลงทะเบียน
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ดึงข้อมูลที่ป้อนในข้อความแก้ไข
                email = etUsername.getText().toString().toLowerCase().trim();
                password = etPassword.getText().toString().trim();
                if (validateInputs()) {
                    login();
                }
            }
        });
    }

    // เรียกใช้กิจกรรมแดชบอร์ดในการเข้าสู่ระบบที่ประสบความสำเร็จ
    private void loadDashboard() {
        Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(i);
        finish();

    }
// แสดงแถบความคืบหน้าขณะลงชื่อเข้าใช้
    private void displayLoader() {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("กำลังเข้าสู่ระบบโปรดรอสักครู่ ...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            //เติมพารามิเตอร์คำร้องขอ
            request.put(KEY_EMAIL, email);
            request.put(KEY_PASSWORD, password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.dismiss();
                        try {
                            //ตรวจสอบว่าผู้ใช้ได้เข้าสู่ระบบเรียบร้อยแล้ว
                            if (response.getInt(KEY_STATUS) == 0) {
                                session.loginUser(email,response.getString(KEY_UNAME));
                                loadDashboard();


                            }else{
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

   // ตรวจสอบอินพุตและแสดงข้อผิดพลาดถ้ามี
    private boolean validateInputs() {
        if(KEY_EMPTY.equals(email)){
            etUsername.setError("กรุณากรอกอีเมล์");
            etUsername.requestFocus();
            return false;
        }
        if(KEY_EMPTY.equals(password)){
            etPassword.setError("กรุณากรอกรหัสผ่าน");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }
}
