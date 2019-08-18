package com.example.bleed_sweat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    private Button singUp;
    EditText inputName,inputPhone,inputPassword;

    private ProgressDialog Loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputName=(EditText)findViewById(R.id.userName);
        inputPhone=(EditText)findViewById(R.id.signUp_phone);
        inputPassword=(EditText)findViewById(R.id.signUp_password);
        singUp=(Button)findViewById(R.id.signUp_btn_2);
        Loading=new ProgressDialog(this);

        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name=inputName.getText().toString();
        String phone=inputPhone.getText().toString();
        String password=inputPassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Write Your Name!!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Input Your Phone Number!!", Toast.LENGTH_SHORT).show();
        }
        else if
            (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Password Not Found", Toast.LENGTH_SHORT).show();
        }

        else
        {
            Loading.setTitle("Signing Up");
            Loading.setMessage("Please Wait..!!");
            Loading.setCanceledOnTouchOutside(false);
            Loading.show();
            CheckPhoneNumber(name,phone,password);
        }



    }

    private void CheckPhoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference mRef;
        mRef= FirebaseDatabase.getInstance().getReference();
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((!dataSnapshot.child("User").child(phone).exists()))
                {
                    HashMap<String,Object> userDataMap=new HashMap<>();
                    userDataMap.put("phone",phone);
                    userDataMap.put("password",password);
                    userDataMap.put("name",name);
                    mRef.child("Users").child(phone).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful())
                          {  Loading.dismiss();
                              Toast.makeText(SignUp.this, "You Are Reistered", Toast.LENGTH_SHORT).show();

                              Intent intent=new Intent(SignUp.this,activity_login.class);
                              startActivity(intent);
                          }
                            else
                          {  Loading.dismiss();
                              Toast.makeText(SignUp.this, "Something Went Wrong Try Again!!", Toast.LENGTH_SHORT).show();
                          }

                        }
                    });

                }
                else {

                    Toast.makeText(SignUp.this, "This" + phone + "Already Exists", Toast.LENGTH_SHORT).show();
                     Loading.dismiss();
                    Toast.makeText(SignUp.this, "Please Use Another Phone Number!!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SignUp.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
