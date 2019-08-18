package com.example.bleed_sweat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class AdminProduct extends AppCompatActivity {

    private String CategoryName,Description,Price,Pname,saveCurrentDate,saveCurrentTime,productRandomKey,DownloadImageUrl;
    private Button  AddNewProduct;
    private ImageView select_product_image;
    private EditText product_price,product_name,product_description;
    private static final int camera=1;
    private Uri imageUri;
    private StorageReference ProductImagesRef;//creating a folder
    private ProgressDialog Loading;
    private DatabaseReference ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product);

        CategoryName=getIntent().getExtras().get("category").toString();
        //it is used here to collect the category type passed in the intent

        select_product_image=(ImageView)findViewById(R.id.select_product_image);
        AddNewProduct=(Button) findViewById(R.id.add_new_product);
        product_price=(EditText) findViewById(R.id.product_price);
        product_name=(EditText) findViewById(R.id.product_name);
        product_description=(EditText) findViewById(R.id.product_description);
        ProductImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef=FirebaseDatabase.getInstance().getReference().child("Products");
        Loading=new ProgressDialog(this);

        select_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });

        AddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ValidateProductdata();
            }
        });

    }

    private void ValidateProductdata() {
        Description=product_description.getText().toString();
        Price=product_price.getText().toString();
        Pname=product_name.getText().toString();

        if(imageUri==null)
        {
            Toast.makeText(this, "Please Add Image!!", Toast.LENGTH_SHORT).show();
        }

       else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please Write The Description", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please Write About Price", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please Write The Name Of The Product", Toast.LENGTH_SHORT).show();
        }
        else
        {   //time nikale ke liye
            StoreProductInformation();



        }
    }

    private void StoreProductInformation() {

        Loading.setTitle("Adding Products");
        Loading.setMessage("Wait Product is Being Updating");
        Loading.setCanceledOnTouchOutside(false);
        Loading.show();
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM,dd,yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");//here a is am
        saveCurrentTime=currentTime.format(calendar.getTime());
        //here now we have to create a key for each product for the storing purpose
        //we can use firebase push method for this also...but here i am just adding current date and time for the procution of new key

        productRandomKey=saveCurrentDate+saveCurrentTime;
        //now i am storing image uri in firebase to store image

        final StorageReference  filePath=ProductImagesRef.child(imageUri.getLastPathSegment()+ productRandomKey + ".jpeg");//lastPathSegment is used to retrive image name

        final UploadTask uploadTask=filePath.putFile(imageUri); //uploading the file

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Loading.dismiss();
                String Message=e.toString();
                Toast.makeText(AdminProduct.this, "Error:" + Message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(AdminProduct.this, "Image Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
                //ab storeage mai save kradi and image link leni hai and database mai store karani hai
                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        DownloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful())
                        {    Loading.dismiss();
                           DownloadImageUrl=task.getResult().toString();//here we are replacing image uri to image link
                            Toast.makeText(AdminProduct.this, "Getting Product Image Url...", Toast.LENGTH_SHORT).show();

                         SaveProductInfoToDataBase();
                        }

                    }
                });


            }
        });



    }

    private void SaveProductInfoToDataBase() {
        HashMap<String,Object> ProductMap=new HashMap<>();
        ProductMap.put("pid",productRandomKey);
        ProductMap.put("time",saveCurrentTime);
        ProductMap.put("description",Description);
        ProductMap.put("image",DownloadImageUrl);
        ProductMap.put("category",CategoryName);
        ProductMap.put("price",Price);
        ProductMap.put("pname",Pname);

        ProductsRef.child(productRandomKey).updateChildren(ProductMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {Loading.dismiss();
                    Toast.makeText(AdminProduct.this, "Product is Uploaded !!", Toast.LENGTH_SHORT).show();

                  Intent intent= new Intent(AdminProduct.this,CategoryActivity.class);
                  startActivity(intent);


                }
                else
                {   Loading.dismiss();
                    Toast.makeText(AdminProduct.this, "Error:" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void OpenGallery() {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,camera);

    }

    //so ab hume uri chayea image ka to store in firebase storage for this

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==camera && resultCode==RESULT_OK&& data!=null){
            imageUri=data.getData();
            select_product_image.setImageURI(imageUri);//posting image on that box
        }
    }


}
