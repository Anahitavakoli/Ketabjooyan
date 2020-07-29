package com.anahitavakoli.ketabjooyan.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.model.Products;
import com.anahitavakoli.ketabjooyan.prevalent.Prevalent;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProductImageDetail;
    private TextView txtProductNameDetail, txtProductDescriptionDetail, txtProductPriceDetail;
    private ElegantNumberButton btnProductNumber;
    private Button btnAddToCart;
    private String productId="" , state="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productId = getIntent().getStringExtra("pid");

        imgProductImageDetail = findViewById(R.id.img_product_detail);
        txtProductNameDetail = findViewById(R.id.txt_product_name_detail);
        txtProductDescriptionDetail = findViewById(R.id.txt_product_description_detail);
        txtProductPriceDetail = findViewById(R.id.txt_product_price_detail);
        btnAddToCart = findViewById(R.id.btn_add_product_detail);
        btnProductNumber = findViewById(R.id.btn_product_number_detail);

        getProductDetail(productId);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(state.equals("order placed") || state.equals("order shipped")){
                    Toast.makeText(ProductDetailActivity.this, "صبر کنید سفارش شما ارسال شود و دوباره ثبت سفارش کنید!", Toast.LENGTH_LONG).show();
                }
                else{
                    addingToCartList();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOrderStatus();
    }

    private void addingToCartList() {

        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final DatabaseReference cartList = FirebaseDatabase.getInstance().getReference().child("cart list");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",productId);
        cartMap.put("pname",txtProductNameDetail.getText().toString());
        cartMap.put("price",txtProductPriceDetail.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("quantity",btnProductNumber.getNumber());
        cartMap.put("discount","");


        cartList.child("user view").child(Prevalent.currentOnlineUser.getPhoneNumber()).child("products").child(productId)
                .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    cartList.child("admin view").child(Prevalent.currentOnlineUser.getPhoneNumber()).child("products").child(productId)
                            .updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProductDetailActivity.this, "به سبد خرید اضافه شد", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ProductDetailActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

    }

    private void getProductDetail(String productID) {

        try {

            DatabaseReference productReference = FirebaseDatabase.getInstance().getReference().child("products");
            productReference.child(productID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        Products products = dataSnapshot.getValue(Products.class);

                        txtProductNameDetail.setText(products.getPname());
                        txtProductDescriptionDetail.setText(products.getPdescription());
                        txtProductPriceDetail.setText(products.getPrice());
                        Picasso.get().load(products.getImage()).into(imgProductImageDetail);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void checkOrderStatus(){

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child(Prevalent.currentOnlineUser.getPhoneNumber());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    String shippingStatus = dataSnapshot.child("state").getValue().toString();


                    if(shippingStatus.equals("shipped")){
                        state = "order shipped";
                    }

                    else if(shippingStatus.equals("not shipped")){
                        state = "order placed";
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
