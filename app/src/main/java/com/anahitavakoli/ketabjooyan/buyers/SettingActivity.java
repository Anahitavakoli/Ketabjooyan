package com.anahitavakoli.ketabjooyan.buyers;

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
import android.widget.TextView;
import android.widget.Toast;

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private CircleImageView imageProfileSetting;
    private EditText inputPhoneNumber, inputFullName, inputAddress;
    private TextView txtChangePhoto, txtSaveSetting, txtCloseSetting;
    private Button securityQuestion;

    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfilePictureReference;
    private String checker = "";
    private StorageTask uploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        storageProfilePictureReference = FirebaseStorage.getInstance().getReference().child("profile picturs");

        imageProfileSetting = findViewById(R.id.img_profile_setting);
        inputPhoneNumber = findViewById(R.id.input_phone_number);
        inputFullName = findViewById(R.id.input_name);
        inputAddress = findViewById(R.id.input_address);
        txtChangePhoto = findViewById(R.id.txt_change_image_setting);
        txtSaveSetting = findViewById(R.id.txt_save_setting);
        txtCloseSetting = findViewById(R.id.txt_close_setting);
        securityQuestion = findViewById(R.id.btn_security_question);

        userInfoDisplay(imageProfileSetting, inputFullName, inputPhoneNumber, inputAddress);


        securityQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent securityIntent = new Intent(SettingActivity.this, ResetPasswordActivity.class);
                securityIntent.putExtra("check","setting");
                startActivity(securityIntent);
            }
        });

        txtCloseSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                finish();
            }
        });


        txtSaveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checker.equals("clicked")){

                    userInfoSaved();

                }

                else{

                    updateOnlyUserInfo();
                }

            }
        });


        txtChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                CropImage.activity(imageUri).setAspectRatio(1,1).start(SettingActivity.this);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            imageProfileSetting.setImageURI(imageUri);
        }

        else{
            Toast.makeText(this, "خطایی رخ داده است. دوباره تلاش کنید!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingActivity.this, SettingActivity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("username" , inputFullName.getText().toString());
        userMap.put("phoneOrder" , inputPhoneNumber.getText().toString());
        userMap.put("address" , inputAddress.getText().toString());
        databaseReference.child(Prevalent.currentOnlineUser.getPhoneNumber()).updateChildren(userMap);

        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
        finish();
    }

    private void userInfoSaved() {

        if(TextUtils.isEmpty(inputFullName.getText().toString())){
            Toast.makeText(this, "نام خود را وارد کنید", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(inputPhoneNumber.getText().toString())){
            Toast.makeText(this, "شماره خود را وارد کنید", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(inputAddress.getText().toString())){
            Toast.makeText(this, "آدرس خود را وارد کنید", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked")){
            uploadImage();
        }
    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("آپلود عکس");
        progressDialog.setMessage("لطفا کمی صبر کنید...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri != null){
            final StorageReference fileRef = storageProfilePictureReference.child(Prevalent.currentOnlineUser.getPhoneNumber() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                         Uri downloadUrl = task.getResult();
                         myUrl = downloadUrl.toString();

                         DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("username" , inputFullName.getText().toString());
                        userMap.put("phoneOrder" , inputPhoneNumber.getText().toString());
                        userMap.put("address" , inputAddress.getText().toString());
                        userMap.put("image" , myUrl);
                        databaseReference.child(Prevalent.currentOnlineUser.getPhoneNumber()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                        finish();
                    }

                    else{
                        progressDialog.dismiss();
                        Toast.makeText(SettingActivity.this, "خطایی رخ داده است", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else{
            Toast.makeText(this, "عکس پروفایل انتخاب نشده است!", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView imageProfileSetting, final EditText inputFullName, final EditText inputPhoneNumber, final EditText inputAddress) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.currentOnlineUser.getPhoneNumber());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists()){

                        String image = dataSnapshot.child("image").getValue().toString();
                        String phoneNumber = dataSnapshot.child("phoneOrder").getValue().toString();
                        String username = dataSnapshot.child("username").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(imageProfileSetting);
                        inputFullName.setText(username);
                        inputPhoneNumber.setText(phoneNumber);
                        inputAddress.setText(address);


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
