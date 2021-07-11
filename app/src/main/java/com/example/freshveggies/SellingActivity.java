package com.example.freshveggies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.freshveggies.Prevalent.Prevalent;
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

import io.paperdb.Paper;

public class SellingActivity extends AppCompatActivity {
    private Button addProduct,wantToBuy;
    private ImageView camera;
    private EditText productName,quantity,cost,pluckedDate,farmerInfo,fname;
    private String name,qua,price,date,info,saveCurrentDate,saveCurrentTime,farname;
    private String productRandomkey,key;
    private DatabaseReference productRef;
    StorageReference mStorageRef;
    public  Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling);
        getSupportActionBar().setTitle("Selling Page");
        Paper.init(this);
        addProduct = findViewById(R.id.addProduct);
        productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        camera = findViewById(R.id.camera);
        productName = findViewById(R.id.productName);
        quantity = findViewById(R.id.quantity);
        cost = findViewById(R.id.cost);
        fname=findViewById(R.id.fname);
        pluckedDate = findViewById(R.id.pluckedDate);
        farmerInfo = findViewById(R.id.farmerInfo);
        wantToBuy = findViewById(R.id.wantToBuy);
        mStorageRef=FirebaseStorage.getInstance().getReference("Images");
        wantToBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BuyActivityNew.class));
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);

            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uri =data.getData();
            camera.setImageURI(uri);
        }
    }
    private void ValidateProductData()
    {
        name=productName.getText().toString();
        price=cost.getText().toString();
        info=farmerInfo.getText().toString();
        qua=quantity.getText().toString();
        date=pluckedDate.getText().toString();
        farname=fname.getText().toString();
        if(uri==null){
            Toast.makeText(this, "Product Image is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(name.isEmpty()){
            Toast.makeText(this, "Name of the Product reqd", Toast.LENGTH_SHORT).show();
        }
        else if(price.isEmpty()){
            Toast.makeText(this, "Price of the Product reqd", Toast.LENGTH_SHORT).show();
        }
        else if(qua.isEmpty()){
            Toast.makeText(this, "Quantity of the Product reqd", Toast.LENGTH_SHORT).show();
        }
        else if(date.isEmpty()){
            Toast.makeText(this, "Plucked date of the Product reqd", Toast.LENGTH_SHORT).show();
        }
        else if(info.isEmpty()){
            Toast.makeText(this, "Phone number reqd", Toast.LENGTH_SHORT).show();
        }
        else if(farname.isEmpty()){
            Toast.makeText(this, "Farmer Name is reqd", Toast.LENGTH_SHORT).show();
        }
        else{
            storeProductInfo();
        }
    }

    private void storeProductInfo() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());
        productRandomkey = saveCurrentDate +" "+ saveCurrentTime;
        StorageReference reference = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(uri));
        reference.putFile(uri)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String msg = e.toString();
                        Toast.makeText(SellingActivity.this, "Not Uploaded" + msg, Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SellingActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl =uri;
                        productRef.child(productRandomkey).child("image").setValue(downloadUrl.toString());
                        saveProductInfo();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SellingActivity.this, "not able to get url", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
    private String getExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    String UserPasswordKey= Paper.book().read(Prevalent.UserPasswordKey);
    private void saveProductInfo(){
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",UserPasswordKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("pname",name);
        productMap.put("quantity",qua);
        productMap.put("price",price);
        productMap.put("info",info);
        productMap.put("pdate",date);
        productMap.put("id",productRandomkey);
        productMap.put("fname",farname);

        productRef.child(productRandomkey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(SellingActivity.this, "Product Added successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String msg=task.getException().toString();
                            Toast.makeText(SellingActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}