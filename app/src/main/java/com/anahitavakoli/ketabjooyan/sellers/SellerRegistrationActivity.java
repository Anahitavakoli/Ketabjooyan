package com.anahitavakoli.ketabjooyan.sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anahitavakoli.ketabjooyan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private Button loginSeller, registration;
    private EditText sellerName, sellerPhone, sellerEmail, sellerPass, sellerAddress;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        loginSeller = findViewById(R.id.btn_seller_login);
        registration = findViewById(R.id.btn_seller_register);

        sellerName = findViewById(R.id.input_seller_name);
        sellerPhone = findViewById(R.id.input_seller_phone);
        sellerEmail = findViewById(R.id.input_seller_email);
        sellerPass = findViewById(R.id.input_seller_password);
        sellerAddress = findViewById(R.id.input_seller_address);
        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();

        loginSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intentLogin);
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();
            }
        });
    }

    private void registerSeller() {

        final String name = sellerName.getText().toString();
        final String phone = sellerPhone.getText().toString();
        final String email = sellerEmail.getText().toString();
        final String password = sellerPass.getText().toString();
        final String address = sellerAddress.getText().toString();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(address)){
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                        progressDialog.setTitle("عضویت");
                        progressDialog.setMessage("لطفا کمی صبر کنید! در حال اعتبارسنجی...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        String sID = auth.getCurrentUser().getUid();

                        HashMap<String, Object> sellerMap = new HashMap<>();
                        sellerMap.put("sID" , sID);
                        sellerMap.put("name" , name);
                        sellerMap.put("phone" , phone);
                        sellerMap.put("email" , email);
                        sellerMap.put("password" , password);
                        sellerMap.put("address" , address);

                        ref.child("sellers").child("sID").updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressDialog.dismiss();
                                Toast.makeText(SellerRegistrationActivity.this, "عضویت با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "فیلدها نباید خالی باشند...", Toast.LENGTH_SHORT).show();
        }
    }
}
