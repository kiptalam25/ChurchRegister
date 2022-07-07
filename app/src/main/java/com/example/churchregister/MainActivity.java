package com.example.churchregister;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchregister.entities.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btn_view, btn_read, btn_save;
    EditText txt_name, txt_dob, txt_email, txt_idno, txt_phone;
    RadioGroup baptism, marriage, gender;
    RadioButton bap;
    TextView textView8;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        // Create a new user with a first and last name
        String userid=getIntent().getStringExtra("userid");
        if(!TextUtils.isEmpty(userid)){
            findMember(userid);
        }

        btn_save.setOnClickListener(v -> {
            addData();
        });
btn_view.setOnClickListener(v->{
    startActivity(new Intent(MainActivity.this,ViewMembers.class));
    finish();
});


        txt_dob.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus)
            try {
                LocalDate date1 = LocalDate.parse(txt_dob.getText().toString().trim());
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                LocalDate date2 = LocalDate.parse(sdf.format(new Date()));
                Period period = date1.until(date2);
                int yearsBetween = period.getYears();

                if(yearsBetween<18){
                    Toast.makeText(MainActivity.this, String.valueOf(yearsBetween), Toast.LENGTH_SHORT).show();
                    txt_idno.setVisibility(View.GONE);
                    textView8.setVisibility(View.GONE);
                }else{
                    txt_idno.setVisibility(View.VISIBLE);
                    textView8.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                txt_dob.setError("Format: yyyy/MM/dd");
//                    Toast.makeText(MainActivity.this, "Wrong DateFormat", Toast.LENGTH_SHORT).show();
                System.out.println("Error" + e.getMessage());
            }
        });
    }

    private void findMember(String userid) {
        DocumentReference docRef=FirebaseFirestore
                .getInstance()
                .collection("members")
                .document(userid);

        docRef.addSnapshotListener((value, error) -> {
           if(value.exists()) {

               Member member=value.toObject(Member.class);
               if (member.getBaptized().toLowerCase().equals("yes")) {
                   int radio_button_Id = baptism.getChildAt(1).getId();
                   baptism.check(radio_button_Id);
               } else {
                   int radio_button_Id = baptism.getChildAt(0).getId();
                   baptism.check(radio_button_Id);
               }
               if (member.getMarital_status().toLowerCase().equals("yes")) {
                   int radio_button_Id = marriage.getChildAt(1).getId();
                   marriage.check(radio_button_Id);
               } else {
                   int radio_button_Id = marriage.getChildAt(0).getId();
                   marriage.check(radio_button_Id);
               }
               txt_name.setText(member.getName());
               txt_dob.setText(member.getDate_of_birth());
               txt_email.setText(member.getEmail());
               txt_idno.setText(member.getIdno());
               txt_phone.setText(member.getPhoneno());
               if (member.getGender().toLowerCase().equals("male")) {
                   int radio_button_Id = gender.getChildAt(0).getId();
                   gender.check(radio_button_Id);
               } else {
                   int radio_button_Id = gender.getChildAt(1).getId();
                   gender.check(radio_button_Id);
               }
               btn_save.setText("Update");
               btn_view=findViewById(R.id.btn_view);
               }
        });
    }

    public void init() {
        baptism = findViewById(R.id.baptism);
        marriage = findViewById(R.id.marriage);
        txt_name = findViewById(R.id.txt_name);
        txt_dob = findViewById(R.id.txt_dob);
        txt_email = findViewById(R.id.txt_email);
        txt_idno = findViewById(R.id.txt_idno);
        txt_phone = findViewById(R.id.txt_phone);
        gender = findViewById(R.id.gender);
        btn_save = findViewById(R.id.btn_save);
         textView8=findViewById(R.id.textView8);
         btn_view=findViewById(R.id.btn_view);
    }

    public void addData() {
        String name = txt_name.getText().toString().trim();
        String idno = txt_idno.getText().toString().trim();
        String phone = txt_phone.getText().toString().trim();
        RadioButton rb = findViewById(marriage.getCheckedRadioButtonId());
        String marital_status = rb.getText().toString();
        RadioButton bap = findViewById(baptism.getCheckedRadioButtonId());
        String baptized = bap.getText().toString();
        RadioButton gen = findViewById(gender.getCheckedRadioButtonId());
        String gender = gen.getText().toString();
        String email = txt_email.getText().toString().trim();
        String date_of_birth = txt_dob.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            txt_name.setError("Fill This FIeld");
            txt_name.requestFocus();
            return;
        }
        if(txt_idno.getVisibility()==View.VISIBLE){
        if (TextUtils.isEmpty(idno)) {
            txt_idno.setError("Fill This FIeld");
            txt_idno.requestFocus();
            return;
        }
        }
        if (TextUtils.isEmpty(phone)) {
            txt_phone.setError("Fill This Field");
            txt_phone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(marital_status)) {
//        marriage.set
//        txt_name.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(baptized)) {
            return;
        }
        if (TextUtils.isEmpty(gender)) {
//        txt_dob.setError("Fill This FIeld");
//        txt_dob.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            txt_email.setError("Fill This FIeld");
            txt_email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(date_of_birth)) {
            txt_dob.setError("Fill This FIeld");
            txt_dob.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(idno)){

            idno=db.collection("members").document().getId();
        }
        Member member = new Member(name, idno, phone, marital_status, baptized, gender, email, date_of_birth);


// Add a new document with a generated ID
        db.collection("members")
                .document(idno)
                .set(member, SetOptions.merge())
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                        Log.w(TAG, "Error adding document", e);
                });
    }


}