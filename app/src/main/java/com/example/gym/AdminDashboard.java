package com.example.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    //private List countlist;
    //private TextView ctotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        //ctotal = (TextView)findViewById(R.id.ctotal);
        //countlist = new ArrayList();

        //firebase auth get instance
        mAuth = FirebaseAuth.getInstance();
//
//        reference = FirebaseDatabase.getInstance().getReference("user");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot ds: snapshot.getChildren()){
//                    countlist.add(ds);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        ctotal.setText(String.valueOf(countlist.size()));
    }

    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(AdminDashboard.this,login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();

    }

    public void tocustomers(View view) {
        Intent intent = new Intent(AdminDashboard.this,Customers_list.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAuth.signOut();
        Intent intent = new Intent(AdminDashboard.this,login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();
    }

    public void toexercise(View view) {
        Intent intent = new Intent(AdminDashboard.this,exercise.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }
}