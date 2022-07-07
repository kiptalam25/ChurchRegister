package com.example.churchregister;

import static android.content.ContentValues.TAG;

import static com.itextpdf.text.Font.FontFamily.HELVETICA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchregister.adapters.MembersAdapter;
import com.example.churchregister.entities.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class ViewMembers extends BaseActivity {

    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    MembersAdapter adapter;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    List<Member> memberList;
    TextView btn_addmore;
    Button btn_print;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members);
memberList=new ArrayList<>();
        recyclerView =findViewById(R.id.myRecycler);
        btn_print=findViewById(R.id.btn_print);
        btn_addmore=findViewById(R.id.btn_addmore);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
btn_addmore.setOnClickListener(v->{
    startActivity(new Intent(ViewMembers.this,MainActivity.class));
    finish();
});
btn_print.setOnClickListener(v->{
    try {
        printToPdf();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (DocumentException e) {
        e.printStackTrace();
    }
});
        readData();
    }

    public void readData() {

        db.collection("members").addSnapshotListener((value, error) -> {
            memberList.clear();
            for (DocumentSnapshot document : value.getDocuments()) {
                Member member = document.toObject(Member.class);
                member.setId(document.getId());
                memberList.add(member);
                adapter = new MembersAdapter(memberList, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

        });
    }

    public void printToPdf() throws IOException, DocumentException {
        if(permission()){

            /**
             * Creating Document
             */
            Document document = new Document();// Location to save

            String filename= System.currentTimeMillis() +".pdf";

            File dir =new File(Environment.getExternalStorageDirectory(), "/ChurchRegister/");
            if (! dir.exists()){
                System.out.println("creating file folder");
                dir.mkdir();
            }
            File file =new File(dir+"/"+filename);
//        File file1 =new File(Environment.getExternalStorageDirectory(), "/Downloads/IMG_20181129_102010.jpg");
            PdfWriter.getInstance(document, new FileOutputStream(file));


// Open to write
            document.open();

            // Document Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("AIC MOSORIOT");


            /***
             * Variables for further use....
             */
            BaseColor mColorAccent = new BaseColor(0, 204, 153, 255);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));


            //   document.add(image);
            InputStream inputStream = ViewMembers.this.getAssets().open(
                    "index.png");
            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image;
            image = Image.getInstance(stream.toByteArray());
            image.setAlignment(Element.ALIGN_CENTER);
            //image.setAbsolutePosition(imageStartX, imageStartY);
            image.scalePercent(100f);
            document.add(image);

//
            // System.setProperty("http.agent", "Chrome");

            long time=new Date().getTime();

            // Title Order Details...
// Adding Title....
            Font.FontFamily font=new Font(HELVETICA).getFamily();
            Font mOrderDetailsTitleFont = new Font(font, 36.0f, Font.NORMAL, BaseColor.BLACK);// Creating Chunk
            Chunk mOrderDetailsTitleChunk = new Chunk("AIC Mosoriot ", mOrderDetailsTitleFont);// Creating Paragraph to add...
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);// Setting Alignment for Heading
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);// Finally Adding that Chunk
            document.add(mOrderDetailsTitleParagraph);


            document.add(new Paragraph(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())));
//            document.add(new Paragraph("To:     "+dateTo.getText().toString()));
            // Fields of Order Details...
// Adding Chunks for Title and value
            Font mOrderIdFont = new Font(font, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Chunk mOrderIdChunk = new Chunk(":", mOrderIdFont);
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);


            Paragraph pa=new Paragraph("");
            pa.setAlignment(Element.ALIGN_RIGHT);
//    document.add(new Paragraph("Clerk"));
            document.add(pa);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));


            int size=memberList.size();
            for( int j=0;j<size;j++){

                final Member data = memberList.get(j);
                Paragraph para=new Paragraph(data.getId());
                para.setAlignment(Element.ALIGN_RIGHT);
                Paragraph name=new Paragraph(data.getName().toUpperCase(Locale.ROOT));
                name.setAlignment(Element.ALIGN_CENTER);
                document.add(name);
                document.add(new Paragraph(
                        "Email:   "+data.getEmail()
                                + "\nBaptized:   "+data.getBaptized()
                                +"\n Gender:   "+data.getGender()
                                +"\n Phone: "+data.getPhoneno()
                                +"\nMarried:    "+data.getMarital_status()
                                +"\nDateOfBirth: "+data.getDate_of_birth()
                                +"   Age: "));


                document.add(para);
                document.add(new Chunk(lineSeparator));
                document.add(new Paragraph("\n"));
            }
            document.add(new Paragraph("Total Members:            "));
            Paragraph p=new Paragraph("This is System Generated Report \n Created On "+new Date());
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(new Paragraph(p));

            document.close();






            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle("Done!");
            alertDialog.setMessage("PDF FILE STORED AT "+dir.getName()+" FOLDER AS "+filename);

            String finalFilename = filename;
            alertDialog.setPositiveButton("Ok.", (dialog, which) -> {
                dialog.cancel();

            });

            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            alertDialog.show();





//
        }else {showSettingsAlert();}
    }

    private  boolean permission(){
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }else{
            return  false;
        }

    }

    public void showSettingsAlert(){


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("STORAGE SETTINGS");

        // Setting Dialog Message
        alertDialog.setMessage("STORAGE ACCESS NOT GRATED. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                ActivityCompat.requestPermissions(ViewMembers.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

//                VerificationsReportActivity.this.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        alertDialog.show();
    }
}