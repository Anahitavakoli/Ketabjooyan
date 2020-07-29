package com.anahitavakoli.ketabjooyan.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.admin.AdminCategoryActivity;
import com.anahitavakoli.ketabjooyan.model.Users;
import com.anahitavakoli.ketabjooyan.prevalent.Prevalent;
import com.anahitavakoli.ketabjooyan.utility.Util;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private EditText username, password;
    private Button login;
    private String dbName = "users";
    private TextView adminLink, notAdminLink, forgetPasswordLink;
    private com.rey.material.widget.CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        username = findViewById(R.id.login_username_input);
        password = findViewById(R.id.login_password_input);
        login = findViewById(R.id.login_btn);
        checkBox = findViewById(R.id.remember_me_chk);
        adminLink = findViewById(R.id.admin_panel_link);
        notAdminLink = findViewById(R.id.not_admin_panel_link);
        forgetPasswordLink = findViewById(R.id.txt_forget_pass);
        findViewById(R.id.rel_layout_login).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        Paper.init(this);

        forgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent securityIntent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                securityIntent.putExtra("check", "login");
                startActivity(securityIntent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText("ورود ادمین");
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                dbName = "admins";
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setText("ورود");
                notAdminLink.setVisibility(View.INVISIBLE);
                adminLink.setVisibility(View.VISIBLE);
                dbName = "users";
            }
        });
    }

    private void loginUser() {

        String userName = username.getText().toString();
        String pass = password.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "نام کاربری یا ایمیل را وارد کنید", Toast.LENGTH_SHORT).show();
            username.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "پسورد را وارد کنید", Toast.LENGTH_SHORT).show();
            password.requestFocus();
        } else {

            progressDialog.setTitle("ورود");
            progressDialog.setMessage("لطفا کمی صبر کنید!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            allowLoginUser(userName, pass);
        }
    }

    private void allowLoginUser(final String user, final String pass) {


        if (checkBox.isChecked()) {
            Paper.book().write(Prevalent.userUsername, user);
            Paper.book().write(Prevalent.userPasswordKey, pass);
        }

        if(dbName.equals("users")){

            final Context context = this;

            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss();
                    try {
                        if(response.getString("message").equals("true")){
                            finish();
                            Intent i = new Intent(context, HomeActivity.class);
                            startActivity(i);
                        }

                        else{
                            Toast.makeText(context, "یوزرنیم یا پسورد اشتباه است", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));

                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            JSONObject parameters = new JSONObject();
            try {
                parameters.put("username", username.getText().toString());
                parameters.put("password", password.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:8080/server2_war_exploded/api/users/loginUser", parameters, listener, errorListener);
            Util.getInstance().addToRequestQueue(request);
        }

    }
}
