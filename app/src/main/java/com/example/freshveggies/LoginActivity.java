package com.example.freshveggies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freshveggies.Model.Users;
import com.example.freshveggies.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText InputNumber, InputPassword;
    private Button LoginButton;
    private TextView forgetpassword;
    private CheckBox chckboxRememberme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = (Button) findViewById(R.id.login_btn);
        InputNumber = (EditText) findViewById(R.id.login_phoneno);
        InputPassword = (EditText) findViewById(R.id.login_pass);
        forgetpassword= (TextView) findViewById(R.id.forgetp);
        chckboxRememberme = (CheckBox) findViewById(R.id.rem_me);
        Paper.init(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }

        });
        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ForPass.class);
                startActivity(intent);

            }
        });
    }

    private void LoginUser() {
        String phone = InputNumber.getText().toString();
        String passw = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(passw)) {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        } else {
            AllowAccessToAccount(phone, passw);
        }
    }

    private void AllowAccessToAccount(String phone, String passw) {

        if (chckboxRememberme.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, passw);

        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(phone).exists()) {
                    Users usersData = snapshot.child("Users").child(phone).getValue(Users.class);
                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(passw)) {
                            Toast.makeText(LoginActivity.this, "Logging in...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, SourceActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Account with this phone number doesnot exists", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}
