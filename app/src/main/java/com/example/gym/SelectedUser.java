package com.example.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectedUser extends AppCompatActivity {
    TextView tvuser,semail,sage,sgender,sbmi,sphone;
    RecyclerView recyclerview;
    List<PaymentClass> paymentClassList;
    String email;
    View view;
    UserPayment_Adapter userPaymentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_user);

        recyclerview = (RecyclerView)findViewById(R.id.rcvpayhistory);
        tvuser = (TextView)findViewById(R.id.selectdusername);
        semail = (TextView)findViewById(R.id.semail);
        sgender = (TextView)findViewById(R.id.sgender);
        sbmi = (TextView)findViewById(R.id.sbmi);
        sphone = (TextView)findViewById(R.id.sphone);
        view = (View)findViewById(R.id.selectedview);


        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        recyclerview.addItemDecoration(itemDecoration);

        paymentClassList = new ArrayList<>();


        Intent intent = getIntent();

        if (intent.getExtras() != null){
            User user = (User) intent.getSerializableExtra("data");
           
            semail.setText(user.getEmail());
            email=user.getEmail();
            //checking few things
            if (user.getFname() == null && user.getLname()==null){
                tvuser.setText("none");
            }else if (user.getFname()!=null && user.getLname() != null){
                tvuser.setText(String.format("%s %s", user.getFname(), user.getLname()));
            }else if (user.getLname()==null && user.getFname()!=null){
                tvuser.setText(user.getFname());
            }else if (user.getFname()==null && user.getLname()!=null){
                tvuser.setText(user.getLname());
            }
            
            //checking few things
            if (user.getGender()==null){
                sgender.setText("none");
            }else {
                sgender.setText(user.getGender());
            }
            
            //checking few things
            if (user.getBmi()==null){
                sbmi.setText("none");
            }else {
                sbmi.setText(String.format("%s kg/m2", user.getBmi()));
            }
            
            //checking few things
            if (user.getPhone()==null){
                sphone.setText("none");
            }else {
                sphone.setText(user.getPhone());
            }
            
        }

        Query query = FirebaseDatabase.getInstance().getReference("payment").orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                run(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void run(DataSnapshot snapshot) {
        for (DataSnapshot ds : snapshot.getChildren()){
            PaymentClass data = ds.getValue(PaymentClass.class);
            paymentClassList.add(data);
            Collections.reverse(paymentClassList);
        }

        if (paymentClassList.size()==0){
            Snackbar snackbar = Snackbar.make(view,"No payment history",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        try {
            //where i initialize recyclerview adapter
            userPaymentAdapter = new UserPayment_Adapter(paymentClassList);
            recyclerview.setAdapter(userPaymentAdapter);

        }catch (Exception e){
            Snackbar snackbar = Snackbar.make(view,"Error: "+e,Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public void dashboard(View view) {
        Intent intent = new Intent(SelectedUser.this,Customers_list.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();
    }
}