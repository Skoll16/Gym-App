package com.example.bleed_sweat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CategoryActivity extends AppCompatActivity {

    private ImageView whey_iso,whey_conc,whey_hydro,whey_raw,
            multi_vitamin,mass_gainer,bcaa,fish_oil,creatine,
            glutamine,dinabol,tetso_boost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        whey_iso=(ImageView)findViewById(R.id.whey_iso);
        whey_conc=(ImageView)findViewById(R.id.whey_conc);
        whey_hydro=(ImageView)findViewById(R.id.whey_hydro);
        whey_raw=(ImageView)findViewById(R.id.whey_raw);
        multi_vitamin=(ImageView)findViewById(R.id.multi_vitamin);
        mass_gainer=(ImageView)findViewById(R.id.mass_gainer);
        bcaa=(ImageView)findViewById(R.id.bcaa);
        fish_oil=(ImageView)findViewById(R.id.fish_oil);
        creatine=(ImageView)findViewById(R.id.creatine);
        glutamine=(ImageView)findViewById(R.id.glutamine);
        dinabol=(ImageView)findViewById(R.id.dinabol);
        tetso_boost=(ImageView)findViewById(R.id.tetso_boost);


        whey_iso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this,AdminProduct.class);
                intent.putExtra("category","whey_iso");
                startActivity(intent);
            }
        });

        whey_conc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this,AdminProduct.class);
                intent.putExtra("category","whey_conc");
                startActivity(intent);
            }
        });
        tetso_boost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this,AdminProduct.class);
                intent.putExtra("category","tetso_boost");
                startActivity(intent);
            }
        });
        dinabol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this,AdminProduct.class);
                intent.putExtra("category","dinabol");
                startActivity(intent);
            }
        });
        glutamine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this,AdminProduct.class);
                intent.putExtra("category","glutamine");
                startActivity(intent);
            }
        });


        whey_hydro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this,AdminProduct.class);
                intent.putExtra("category","whey_hydro");
                startActivity(intent);
            }
        });

        whey_raw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this,AdminProduct.class);
                intent.putExtra("category","whey_raw");
                startActivity(intent);
            }
        });

        multi_vitamin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this,AdminProduct.class);
                intent.putExtra("category","multi_vitamin");
                startActivity(intent);
            }
        });

        mass_gainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this,AdminProduct.class);
                intent.putExtra("category","mass_gainer");
                startActivity(intent);
            }
        });

        bcaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this,AdminProduct.class);
                intent.putExtra("category","bcaa");
                startActivity(intent);
            }
        });

        fish_oil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this,AdminProduct.class);
                intent.putExtra("category","fish_oil");
                startActivity(intent);
            }
        });

        creatine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CategoryActivity.this,AdminProduct.class);
                intent.putExtra("category","creatine");
                startActivity(intent);
            }
        });

    }
}
