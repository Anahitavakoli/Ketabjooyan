package com.anahitavakoli.ketabjooyan.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.model.Users;
import com.anahitavakoli.ketabjooyan.prevalent.Prevalent;
import com.anahitavakoli.ketabjooyan.sellers.SellerRegistrationActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button mainLogin, mainSignUp;
    private ProgressDialog progressDialog;
    private TextView txtImSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLogin = findViewById(R.id.main_login_btn);
        mainSignUp = findViewById(R.id.main_join_btn);
        txtImSeller = findViewById(R.id.txt_im_seller);

        progressDialog = new ProgressDialog(this);

        Paper.init(this);

        txtImSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });

        mainLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        mainSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        String userPhoneNumber = Paper.book().read(Prevalent.userPhoneNumberKey);
        String userPassword = Paper.book().read(Prevalent.userPasswordKey);


        if(userPhoneNumber != "" && userPassword != ""){
            if(!TextUtils.isEmpty(userPassword) && !TextUtils.isEmpty(userPhoneNumber)){

                progressDialog.setTitle("ورود");
                progressDialog.setMessage("لطفا کمی صبر کنید!");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


                allowAccess(userPhoneNumber,userPassword);
            }
        }
    }

    private void allowAccess(final String number, final String pass) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("users").child(number).exists()){

                    Users userData = dataSnapshot.child("users").child(number).getValue(Users.class);

                    if(userData.getPhoneNumber().equals(number)){
                        if(userData.getPassword().equals(pass)){

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = userData;
                            Prevalent.userUsername = dataSnapshot.child("users").child(number).child("username").getValue().toString();
                            startActivity(intent);
                            progressDialog.dismiss();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
