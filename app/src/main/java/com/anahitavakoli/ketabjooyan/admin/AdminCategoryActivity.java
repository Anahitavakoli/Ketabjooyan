package com.anahitavakoli.ketabjooyan.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.anahitavakoli.ketabjooyan.buyers.HomeActivity;
import com.anahitavakoli.ketabjooyan.buyers.MainActivity;
import com.anahitavakoli.ketabjooyan.R;

public class AdminCategoryActivity extends AppCompatActivity {

    ImageView lawBook, medicalBook, psychologyBook;
    ImageView artBook, engineeringBook, languageBook;

    private Button adminCheckOrder, adminLogout, adminMaintain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        lawBook = findViewById(R.id.law);
        medicalBook = findViewById(R.id.medical);
        psychologyBook = findViewById(R.id.psychology);
        artBook = findViewById(R.id.art);
        engineeringBook = findViewById(R.id.engineering);
        languageBook = findViewById(R.id.language);
        adminCheckOrder = findViewById(R.id.btn_admin_check_orders);
        adminLogout = findViewById(R.id.btn_admin_logout);
        adminMaintain = findViewById(R.id.btn_admin_maintain_products);

        adminMaintain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent maintainIntent = new Intent(AdminCategoryActivity.this , HomeActivity.class);
                maintainIntent.putExtra("admin", "admin");
                startActivity(maintainIntent);
            }
        });

        adminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logoutIntent = new Intent(AdminCategoryActivity.this , MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                finish();
            }
        });

       adminCheckOrder.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent logoutIntent = new Intent(AdminCategoryActivity.this , AdminNewOrdersActivity.class);
               startActivity(logoutIntent);
           }
       });


        lawBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Law Book");
                startActivity(intent);
            }
        });

        medicalBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Medical Book");
                startActivity(intent);
            }
        });

        psychologyBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Psychology Book");
                startActivity(intent);
            }
        });

        artBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Art Book");
                startActivity(intent);
            }
        });

        engineeringBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Engineering Book");
                startActivity(intent);
            }
        });

        languageBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "Language Book");
                startActivity(intent);
            }
        });

    }
}
