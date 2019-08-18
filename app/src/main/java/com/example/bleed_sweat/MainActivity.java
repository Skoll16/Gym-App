package com.example.bleed_sweat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bleed_sweat.Model.Users;
import com.example.bleed_sweat.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button signUp,logIn;
  private ProgressDialog Loading;
    private String ParentDB="Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUp=(Button)findViewById(R.id.join_btn);
        logIn=(Button)findViewById(R.id.login_btn);
        Paper.init(this);
        Loading=new ProgressDialog(this);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,activity_login.class);
                startActivity(intent);
                finish();


            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
                finish();


            }
        });


        String UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey!="" && UserPasswordKey!="") {
        if(TextUtils.isEmpty(UserPhoneKey)&&TextUtils.isEmpty(UserPasswordKey)) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
        }
           else{
                Loading.setTitle("Logging In");
                Loading.setMessage("Please Wait!!");
                Loading.setCanceledOnTouchOutside(false);
                Loading.show();
                AllowAccess(UserPhoneKey, UserPasswordKey);
               }
        }


        }


    private void AllowAccess(final String phone, final String password)
    {
        final DatabaseReference mRef;
        mRef= FirebaseDatabase.getInstance().getReference();

        //checking if user exist or not
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phone).exists())
                {
                    Users userData=dataSnapshot.child("Users").child(phone).getValue(Users.class);
                    /*here above we had taken phone number from firebase console and just store in the class Users that
                    we had created  also we can retrive data from getphone and getpassword function in Class Users*/

                    //we again check for the user
                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {

                            Loading.dismiss();
                            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);


                        }
                        else
                        {  Loading.dismiss();
                            Toast.makeText(MainActivity.this, "Logged In Failed!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else
                {  Loading.dismiss();
                    Toast.makeText(MainActivity.this, "User Do Not Exist!!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
