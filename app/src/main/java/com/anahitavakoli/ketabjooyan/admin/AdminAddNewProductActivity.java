package com.anahitavakoli.ketabjooyan.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.anahitavakoli.ketabjooyan.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String categoryName, pName, pDescription, pPrice, saveDate, saveTime;

    private ImageView uploadPhoto;
    private EditText productName, productDescription, productPrice;
    private Button addProduct;
    private static final int galleryPick = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageUri;
    private StorageReference productImagesRef;
    private DatabaseReference productRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        categoryName = getIntent().getExtras().get("category").toString();

        progressDialog = new ProgressDialog(this);

        uploadPhoto = findViewById(R.id.upload_product_image);
        productName = findViewById(R.id.product_name_input);
        productDescription = findViewById(R.id.product_description_input);
        productPrice = findViewById(R.id.product_price_input);
        addProduct = findViewById(R.id.add_product_btn);
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("products");



        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });
    }

    private void openGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryPick && resultCode==RESULT_OK && data != null){
            imageUri = data.getData();
            uploadPhoto.setImageURI(imageUri);
        }
    }

    private void validateProductData() {

        pName = productName.getText().toString();
        pDescription = productDescription.getText().toString();
        pPrice = productPrice.getText().toString();

        if(imageUri == null){
            Toast.makeText(this, "عکس محصول را آپلود کنید...", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(pName)){
            Toast.makeText(this, "نام محصول را وارد کنید...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pDescription)){
            Toast.makeText(this, "توضیحات محصول را وارد کنید...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(pPrice)){
            Toast.makeText(this, "قیمت محصول را وارد کنید...", Toast.LENGTH_SHORT).show();
        }

        else{
            storeProductInformation();
        }

    }

    private void storeProductInformation() {

        progressDialog.setTitle("ثبت محصول جدید");
        progressDialog.setMessage("لطفا کمی صبر کنید!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyy");
        saveDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveTime = currentTime.format(calendar.getTime());

        productRandomKey = saveDate + saveTime;

        final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String errorMessage = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error:" + errorMessage, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "تصویر آپلود شد :)", Toast.LENGTH_SHORT).show();
            }
        });

        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }

                downloadImageUri = filePath.getDownloadUrl().toString();
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){

                    downloadImageUri = task.getResult().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "url محصول گرفته شد", Toast.LENGTH_SHORT).show();
                    saveProductInfoToDatabase();
                }
            }
        });
    }



    private void saveProductInfoToDatabase() {
        HashMap<String,Object> productInfoMap = new HashMap<>();

        productInfoMap.put("pid" , productRandomKey);
        productInfoMap.put("date" , saveDate);
        productInfoMap.put("time" , saveTime);
        productInfoMap.put("pname" , pName);
        productInfoMap.put("category" , categoryName);
        productInfoMap.put("price" , pPrice);
        productInfoMap.put("image" , downloadImageUri);
        productInfoMap.put("pdescription" , pDescription);

        productRef.child(productRandomKey).updateChildren(productInfoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    progressDialog.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "محصول در دیتابیس ذخیره شد", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);
                }
                else{
                    progressDialog.dismiss();
                    String error = task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
