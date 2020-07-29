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
import android.widget.Toast;

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.utility.SharedPreferencesHandler;
import com.anahitavakoli.ketabjooyan.utility.Util;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button register;
    private EditText username, phone_number, input_password, input_email, input_password_confirm ;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.register_username);
        phone_number = findViewById(R.id.register_phone_number_input);
        input_password = findViewById(R.id.register_password_input);
        input_email = findViewById(R.id.register_email_input);
        input_password_confirm = findViewById(R.id.register_password_confirm_input);

        register = findViewById(R.id.register_btn);

        progressDialog = new ProgressDialog(this);

        findViewById(R.id.rel_layout_register).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAccount();
            }
        });
    }

    private void validateAccount() {

        String userName = username.getText().toString();
        String phoneNumber = phone_number.getText().toString();
        String password = input_password.getText().toString();
        String passwordConfirm = input_password_confirm.getText().toString();
        String email = input_email.getText().toString();

        if(TextUtils.isEmpty(userName)){
            Toast.makeText(this, "لطفا نام کاربری خود را انتخاب کنید", Toast.LENGTH_SHORT).show();
            username.requestFocus();
        }
        else if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this, "لطفا شماره تلفن خود را وارد کنید", Toast.LENGTH_SHORT).show();
            phone_number.requestFocus();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "لطفا پسورد خود را وارد کنید", Toast.LENGTH_SHORT).show();
            input_password.requestFocus();
        }
        else if(TextUtils.isEmpty(passwordConfirm)){
            Toast.makeText(this, "لطفا پسورد خود را دوباره وارد کنید", Toast.LENGTH_SHORT).show();
            input_password_confirm.requestFocus();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "لطفا ایمیل خود را وارد کنید", Toast.LENGTH_SHORT).show();
            input_email.requestFocus();
        }
        else if(!password.equals(passwordConfirm)){
            Toast.makeText(this, "پسورد و تکرار آن یکی نمی باشد", Toast.LENGTH_SHORT).show();
        }

        else{
            progressDialog.setTitle("عضویت");
            progressDialog.setMessage("لطفا کمی صبر کنید! در حال اعتبارسنجی...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            createAccount();
        }


    }

    private void createAccount(){

        final Context context = this;

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    if(response.getString("token").contains("#") && response.getString("message").equals("ok")){

                        SharedPreferencesHandler spf = SharedPreferencesHandler.getSpf(getApplicationContext());

                        Map<String,String> m = new HashMap<>();
                        m.put("token", response.getString("token"));

                        spf.saveInfoData("myToken",m);

                        finish();
                        Intent i = new Intent(context, HomeActivity.class);
                        startActivity(i);
                    }

                    else{
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
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
            parameters.put("phoneNumber", phone_number.getText().toString());
            parameters.put("email", input_email.getText().toString());
            parameters.put("password", input_password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:8080/server2_war_exploded/api/users/addUser", parameters, listener, errorListener);
        Util.getInstance().addToRequestQueue(request);


    }
}
