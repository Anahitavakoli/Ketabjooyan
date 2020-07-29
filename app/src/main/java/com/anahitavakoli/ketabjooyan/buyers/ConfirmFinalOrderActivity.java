package com.anahitavakoli.ketabjooyan.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText inputShipmentName, inputShipmentPhone, inputShipmentCity, inputShipmentAddress;
    private Button btnConfirm;
    private String totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);


        totalAmount = getIntent().getStringExtra("total price");
        Toast.makeText(this, totalAmount, Toast.LENGTH_SHORT).show();

        inputShipmentName = findViewById(R.id.input_shipping_name);
        inputShipmentPhone = findViewById(R.id.input_shipping_phone);
        inputShipmentCity = findViewById(R.id.input_shipping_city);
        inputShipmentAddress = findViewById(R.id.input_shipping_address);

        btnConfirm = findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });


    }

    private void check() {

        if(TextUtils.isEmpty(inputShipmentName.getText().toString())){
            Toast.makeText(this, "نام دریافت کننده را وارد کنید", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(inputShipmentPhone.getText().toString())){
            Toast.makeText(this, "شماره تلفن دریافت کننده را وارد کنید", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(inputShipmentCity.getText().toString())){
            Toast.makeText(this, "شهر سکونت دریافت کننده را وارد کنید", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(inputShipmentAddress.getText().toString())){
            Toast.makeText(this, "آدرس دقیق دریافت کننده را وارد کنید", Toast.LENGTH_SHORT).show();
        }
        else{
            confirmOrder();
        }
    }

    private void confirmOrder() {

        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child(Prevalent.currentOnlineUser.getPhoneNumber());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount" , totalAmount);
        ordersMap.put("name" , inputShipmentName.getText().toString());
        ordersMap.put("phone" , inputShipmentPhone.getText().toString());
        ordersMap.put("city" , inputShipmentCity.getText().toString());
        ordersMap.put("address" , inputShipmentAddress.getText().toString());
        ordersMap.put("date" , saveCurrentDate);
        ordersMap.put("time" , saveCurrentTime);
        ordersMap.put("state" , "not shipped");

        orderRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("cart list").child("user view").child(Prevalent.currentOnlineUser.getPhoneNumber())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(ConfirmFinalOrderActivity.this, "سفارش شما با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();

                            Intent confirmIntent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                            confirmIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(confirmIntent);
                            finish();
                        }
                    });
                }
            }
        });


    }
}
