package com.example.bleed_sweat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
 private Button Logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

      Logout=(Button)findViewById(R.id.logout);

      Logout.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Paper.book().destroy();

              Intent intent=new Intent(HomeActivity.this,MainActivity.class);
              startActivity(intent);
              finish();
          }
      });
    }

}
