package com.example.churchregister;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
Button btn_create_account;
EditText txt_create_username,txt_create_password;
FirebaseAuth mAuth=FirebaseAuth.getInstance();
TextView btn_login_form;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_create_account=findViewById(R.id.btn_create_account);
        txt_create_password=findViewById(R.id.txt_create_password);
        txt_create_username=findViewById(R.id.txt_create_username);
        btn_login_form=findViewById(R.id.btn_login_form);

        btn_login_form.setOnClickListener(v->{
            startActivity(new Intent(Register.this,Login.class));
            finish();
        });
        btn_create_account.setOnClickListener(v->{
            String email=txt_create_username.getText().toString().trim();
            String password= txt_create_password.getText().toString().trim();
            createAccount(email,password);
        });
    }

    public void createAccount(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(Register.this,Login.class));
                        finish();
                    } else {
                        Toast.makeText(Register.this, task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}