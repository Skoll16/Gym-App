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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bleed_sweat.Model.Users;
import com.example.bleed_sweat.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import org.w3c.dom.Text;

import io.paperdb.Paper;

public class
activity_login extends AppCompatActivity {

    private EditText loginPhone,loginPassword;
    private Button login;
    private ProgressDialog Loading;
    private CheckBox rememberMe;
    private TextView Admin,NotAdmin;

    private String ParentDB="Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Admin=(TextView)findViewById(R.id.admin_panel);
        NotAdmin=(TextView)findViewById(R.id.not_admin_panel);

        login=(Button)findViewById(R.id.login_btn_2);
        loginPhone=(EditText) findViewById(R.id.login_phone);
        loginPassword=(EditText)findViewById(R.id.login_password);
        rememberMe=(CheckBox)findViewById(R.id.remember_me);
        Paper.init(this);
        Loading=new ProgressDialog(this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

          Admin.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {

                  login.setText("Login Admin");
                  Admin.setVisibility(View.INVISIBLE);
                  NotAdmin.setVisibility(View.VISIBLE);
                  ParentDB="Admins";

              }
          });

          NotAdmin.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  login.setText("Login");
                  Admin.setVisibility(View.VISIBLE);
                  NotAdmin.setVisibility(View.INVISIBLE);
                  ParentDB="Users";
              }
          });

    }

    private void loginUser() {
      String phone=loginPhone.getText().toString();
      String password=loginPassword.getText().toString();

      if(TextUtils.isEmpty(phone))
      {
          Toast.makeText(this, "Kindly Enter the Number", Toast.LENGTH_SHORT).show();
      }
      else if(TextUtils.isEmpty(password)){
          Toast.makeText(this, "Kindly Enter The Password", Toast.LENGTH_SHORT).show();

        }

      else{
          Loading.setTitle("Signing In");
          Loading.setMessage("Please Wait!!");
          Loading.setCanceledOnTouchOutside(false);
          Loading.show();

        AllowAccess(phone,password);
      }

    }

    private void AllowAccess(final String phone, final String password) {

        if(rememberMe.isChecked()){
            //here we storing info to prevalent class
        Paper.book().write(Prevalent.UserPhoneKey,phone);
        Paper.book().write(Prevalent.UserPasswordKey,password);
        }

        DatabaseReference mRef;
        mRef= FirebaseDatabase.getInstance().getReference();

        //checking if user exist or not
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(ParentDB).child(phone).exists())
                {
                    Users userData=dataSnapshot.child(ParentDB).child(phone).getValue(Users.class);
                    /*here above we had taken phone number from firebase console and just store in the class Users that
                    we had created  also we can retrive data from getphone and getpassword function in Class Users*/

                    //we again check for the user
                    if(userData.getPhone().equals(phone))
                    {
                        if(userData.getPassword().equals(password))
                        {
                            if(ParentDB.equals("Admins"))
                            {
                                Toast.makeText(activity_login.this, "Signed In Successfully!!", Toast.LENGTH_SHORT).show();
                                Loading.dismiss();
                                Intent intent=new Intent(activity_login.this,CategoryActivity.class);
                                startActivity(intent);
                            }


                        }
                        else if(ParentDB.equals("Users"))
                        {
                            Toast.makeText(activity_login.this, "Signed In Successfully!!", Toast.LENGTH_SHORT).show();
                            Loading.dismiss();
                            Intent intent=new Intent(activity_login.this,HomeActivity.class);
                            startActivity(intent);
                        }

                        else
                        {  Loading.dismiss();
                            Toast.makeText(activity_login.this, "Incorrect Password!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else
                {  Loading.dismiss();
                    Toast.makeText(activity_login.this, "User Do Not Exist!!", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
