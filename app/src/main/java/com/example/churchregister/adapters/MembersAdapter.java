package com.example.churchregister.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.churchregister.MainActivity;
import com.example.churchregister.R;
import com.example.churchregister.entities.Member;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder> {
    private List<Member> memberList;
    public Context context;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    public MembersAdapter(List<Member> memberList, Context context) {
        this.memberList = memberList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.members_adapter, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, int i) {
        try {
            final Member data = memberList.get(i);

            viewHolder.btn_delete.setOnClickListener(v ->
            {
                 AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Alert!");
                alertDialog.setMessage("Are You Sure You Want To Remove "+data.getName().toUpperCase()+" ?");
                alertDialog.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
                alertDialog.setPositiveButton("Yes", (dialog, which) ->
                    delete(data.getId()));
                AlertDialog dialog = alertDialog.create();
                dialog.show();
            });
            viewHolder.btn_edit.setOnClickListener(v->{
                Intent myintent=new Intent(context, MainActivity.class);
                myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                myintent.putExtra("userid", data.getId());
                context.startActivity(myintent);

            });

            viewHolder.email.setText(data.getEmail());
            viewHolder.name.setText(data.getName().toUpperCase(Locale.ROOT));
            viewHolder.idno.setText("ID." + data.getIdno() + ".00");
            viewHolder.phone.setText(data.getPhoneno());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


            LocalDate date1 = LocalDate.parse(sdf.parse(data.getDate_of_birth()).toString());
            LocalDate date2 = LocalDate.parse(sdf.format(new Date()));
            Period period = date1.until(date2);
            int yearsBetween = period.getYears();

            viewHolder.age.setText(yearsBetween);



        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, idno, age, email, phone, btn_delete,btn_edit;
        public LinearLayout linearLayout;
        public CardView cardView;
        RecyclerView.ViewHolder viewHolder;


        private MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.disp_name);
            phone = itemView.findViewById(R.id.phone);
            idno = itemView.findViewById(R.id.disp_idno);
            email = itemView.findViewById(R.id.email);
            age = itemView.findViewById(R.id.age);
            btn_delete=itemView.findViewById(R.id.btn_delete);
            btn_edit=itemView.findViewById(R.id.btn_edit);





        }




    }
    public void delete(String documentPath){
        db.collection("members").document(documentPath)
                .delete()
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "DocumentSnapshot successfully deleted!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Error deleting document", Toast.LENGTH_SHORT).show());
    }
}