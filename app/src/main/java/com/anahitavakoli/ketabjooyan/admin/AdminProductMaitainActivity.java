package com.anahitavakoli.ketabjooyan.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.anahitavakoli.ketabjooyan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminProductMaitainActivity extends AppCompatActivity {

    ImageView editImage;
    EditText pnameEdit, priceEdit, descriptionEdit;
    Button applyChange, deleteProduct;
    private String productID="";
    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_maitain);

        productID = getIntent().getStringExtra("pid");
        productRef = FirebaseDatabase.getInstance().getReference().child("products").child(productID);

        editImage = findViewById(R.id.product_image_edit);
        pnameEdit = findViewById(R.id.product_name_edit);
        priceEdit = findViewById(R.id.product_price_edit);
        descriptionEdit = findViewById(R.id.product_description_edit);
        applyChange = findViewById(R.id.apply_change_btn);
        deleteProduct = findViewById(R.id.delete_product_btn);

        displaySpecificProduct();

        applyChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisProduct();
            }
        });
    }

    private void deleteThisProduct() {

        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Intent i = new Intent(AdminProductMaitainActivity.this, AdminCategoryActivity.class);
                startActivity(i);
                finish();

                Toast.makeText(AdminProductMaitainActivity.this, "محصول با موفقیت پاک شد.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges() {
        String Pname = pnameEdit.getText().toString();
        String Pdescription = descriptionEdit.getText().toString();
        String Pprice = priceEdit.getText().toString();

        if(TextUtils.isEmpty(Pname)){
            Toast.makeText(this, "نام محصول نباید خالی باشد", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(Pprice)){
            Toast.makeText(this, "قیمت محصول نباید خالی باشد", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pdescription)){
            Toast.makeText(this, "توضیح محصول نباید خالی باشد", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String,Object> productInfoMap = new HashMap<>();

            productInfoMap.put("pid" , productID);
            productInfoMap.put("pname" , Pname);
            productInfoMap.put("price" , Pprice);
            productInfoMap.put("pdescription" , Pdescription);

            productRef.updateChildren(productInfoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(AdminProductMaitainActivity.this, "محصول آپدیت شد", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AdminProductMaitainActivity.this, AdminCategoryActivity.class);
                        startActivity(i);
                        finish();
                    }

                }
            });
        }

    }

    private void displaySpecificProduct() {

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String productName = dataSnapshot.child("pname").getValue().toString();
                    String productPrice = dataSnapshot.child("price").getValue().toString();
                    String productDescription = dataSnapshot.child("pdescription").getValue().toString();
                    String productImage = dataSnapshot.child("image").getValue().toString();

                    pnameEdit.setText(productName);
                    descriptionEdit.setText(productDescription);
                    priceEdit.setText(productPrice);
                    Picasso.get().load(productImage).into(editImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
