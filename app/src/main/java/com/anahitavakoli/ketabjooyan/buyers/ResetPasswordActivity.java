package com.anahitavakoli.ketabjooyan.buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anahitavakoli.ketabjooyan.R;
import com.anahitavakoli.ketabjooyan.prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, pageInfo;
    private EditText phoneNumber, questionCity, questionFriend;
    private Button continueResetPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        pageTitle = findViewById(R.id.txt_reset_pass);
        pageInfo = findViewById(R.id.txt_q_reset_pass);
        phoneNumber = findViewById(R.id.input_reset_pass_num);
        questionCity = findViewById(R.id.input_reset_pass_q1);
        questionFriend = findViewById(R.id.input_reset_pass_q2);
        continueResetPass = findViewById(R.id.reset_pass_verify_btn);

        check = getIntent().getStringExtra("check");
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(check.equals("setting")){

            pageTitle.setText("سوال امنیتی");
            pageInfo.setText("برای امکان تغییر پسورد نیاز است این دو سوال را پاسخ دهید!");
            phoneNumber.setVisibility(View.GONE);
            continueResetPass.setText("ذخیره");

            displayPreAnswers();

            continueResetPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(questionCity.getText().toString()) && TextUtils.isEmpty(questionFriend.getText().toString())){
                        Toast.makeText(ResetPasswordActivity.this, "سوال امنیتی را پاسخ دهید", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        saveAnswers();
                    }
                }
            });

        }
        else if(check.equals("login")){

            continueResetPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });
        }
    }

    private void verifyUser() {

        final String phone = phoneNumber.getText().toString();
        final String AnsCity = questionCity.getText().toString();
        final String AnsFriend = questionFriend.getText().toString();

        if(TextUtils.isEmpty(phone) && TextUtils.isEmpty(AnsCity) && TextUtils.isEmpty(AnsFriend)){
            Toast.makeText(ResetPasswordActivity.this, "فیلدهای خواسته شده را پر کنید!", Toast.LENGTH_SHORT).show();
        }

        else{


            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(phone);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){


                        if(dataSnapshot.hasChild("securityQuestion")){

                            String city = dataSnapshot.child("securityQuestion").child("answerCity").getValue().toString();
                            String friend = dataSnapshot.child("securityQuestion").child("answerFriend").getValue().toString();

                            if(!city.equals(AnsCity) || !friend.equals(AnsFriend)){
                                Toast.makeText(ResetPasswordActivity.this, "پاسخ داده شده درست نیست!", Toast.LENGTH_SHORT).show();
                            }

                            else{

                                final AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("تغییر پسورد");

                                final EditText newPass = new EditText(ResetPasswordActivity.this);
                                newPass.setHint("پسورد جدید");

                                builder.setView(newPass);

                                builder.setPositiveButton("تغییر", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!newPass.getText().toString().equals("")){
                                            ref.child("password").setValue(newPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                        Toast.makeText(ResetPasswordActivity.this, "رمز با موفقیت تغییر کرد.", Toast.LENGTH_SHORT).show();

                                                        Intent i = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                        startActivity(i);
                                                        finish();

                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                                builder.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }

                        }

                        else {
                            Toast.makeText(ResetPasswordActivity.this, "شما سوال امنیتی را پر نکرده اید.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else{
                        Toast.makeText(ResetPasswordActivity.this, "این شماره ثبت نشده است.", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    private void saveAnswers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.currentOnlineUser.getPhoneNumber());

        HashMap<String, Object> questionMap = new HashMap<>();
        questionMap.put("answerCity", questionCity.getText().toString());
        questionMap.put("answerFriend", questionFriend.getText().toString());
        ref.child("securityQuestion").updateChildren(questionMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ResetPasswordActivity.this, "پاسخ ها ثبت شدند...", Toast.LENGTH_SHORT).show();
                    Intent intentAnswer = new Intent(ResetPasswordActivity.this , HomeActivity.class);
                    startActivity(intentAnswer);
                }
            }
        });
    }

    private void displayPreAnswers(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.currentOnlineUser.getPhoneNumber());

        ref.child("securityQuestion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String city = dataSnapshot.child("answerCity").getValue().toString();
                    String friend = dataSnapshot.child("answerFriend").getValue().toString();

                    questionCity.setText(city);
                    questionFriend.setText(friend);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
