package com.example.freshveggies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class ForPass extends AppCompatActivity {
    private EditText inname,inpass,inrepass;
    private Button sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_pass);

        sub=(Button) findViewById(R.id.re_btn);
        inname=(EditText) findViewById(R.id.re_phoneno);
        inpass=(EditText) findViewById(R.id.re_pass);
        inrepass=(EditText) findViewById(R.id.re_repass);

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAb();
            }
        });
    }

    private void createAb() {
        String phoneno=inname.getText().toString();
        String passwo=inpass.getText().toString();
        String repassw=inrepass.getText().toString();

        if(TextUtils.isEmpty(phoneno)){
            Toast.makeText(this,"Please enter phone number...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(passwo)){
            Toast.makeText(this,"Please enter new password...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(repassw)){
            Toast.makeText(this,"Please reenter new password...",Toast.LENGTH_SHORT).show();
        }
        else
        {
            ValidatephoneNumber(phoneno,passwo,repassw);

        }



    }

    private void ValidatephoneNumber(String phoneno, String passwo, String repassw) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(phoneno).exists())
                {
                    HashMap<String,Object> userdataMap=new HashMap<>();
                    userdataMap.put("password",passwo);

                    RootRef.child("Users").child(phoneno).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForPass.this, "New Password updated", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ForPass.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(ForPass.this, "network error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(ForPass.this, "This "+ phoneno+"does not exits..", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ForPass.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }

}
