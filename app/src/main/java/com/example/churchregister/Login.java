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

public class Login extends AppCompatActivity {
FirebaseAuth mAuth=FirebaseAuth.getInstance();
Button btn_singnin;
EditText txt_username,txt_password;
TextView btn_open_registration;
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(Login.this,ViewMembers.class));
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_singnin=findViewById(R.id.btn_singnin);
        txt_password=findViewById(R.id.txt_password);
        txt_username=findViewById(R.id.txt_username);
        btn_open_registration =findViewById(R.id.btn_open_registration);

        btn_singnin.setOnClickListener(v->{
          String email=  txt_username.getText().toString().trim();
          String password= txt_password.getText().toString().trim();
          login(email,password);
        });

        btn_open_registration.setOnClickListener(v->{
            startActivity(new Intent(Login.this,Register.class));
            finish();
        });

    }

    public void login(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(Login.this,ViewMembers.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}