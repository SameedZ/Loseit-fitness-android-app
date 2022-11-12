package com.example.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;

public class bmi extends AppCompatActivity {
    private EditText height,weight;
    private Button calculate;
    private TextView display;
    private View bmiview;
    private String email;
    private String keyId;
    private AVLoadingIndicatorView bmiavi;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mdatabase;
    private DatabaseReference reference;

    private Double Height;
    private Double Weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        //overridePendingTransition(R.anim.enter_from_right, R.anim.exit_through_right);

        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        reference = mdatabase.getReference("user");

        bmiview = (View)findViewById(R.id.bmiview);

        bmiavi = (AVLoadingIndicatorView)findViewById(R.id.bmiavi);

        //linking to xml
        height = (EditText)findViewById(R.id.height);
        weight = (EditText)findViewById(R.id.weight);
        calculate = (Button)findViewById(R.id.bmicalculate);
        display = (TextView)findViewById(R.id.displaybmi);

        //setting onclick listener for button
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting the values from the edit text
                String strheight = height.getText().toString();
                String strweight = weight.getText().toString();


                if (!TextUtils.isEmpty(strheight) && !TextUtils.isEmpty(strweight)){

                    try {
                        Height = Double.parseDouble(strheight);
                        Weight = Double.parseDouble(strweight);
                    }catch (NumberFormatException e){
                        Snackbar snackbar = Snackbar.make(v,"Invalid input",Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                    //performing input validations
                    if (Height != null && Weight != null){
                        //if fields are not empty
                        if (Height >0 && Weight >0){
                            //if height and weight are greater than zero
                            //do the calculation
                            Double bmi = Weight / (Height * Height);

                            display.setText(String.format("%.2f",bmi )+" kg/m2");
                            //update user profile with bmi value
                            startAnim();
                            updatebmi(bmi,keyId,v);
                            //clear values in edit text
                            weight.setText(""); height.setText("");
                        }else {
                            height.setError("");
                            weight.setError("");
                            Snackbar snackbar = Snackbar.make(v,"Height and Weight values must be > 0",Snackbar.LENGTH_LONG);
                            snackbar.show();
                            height.requestFocus();
                        }
                    }else {
                        height.setError("");
                        weight.setError("");
                        height.requestFocus();weight.requestFocus();
                    }
                }else {
                    Snackbar snackbar = Snackbar.make(v,"Enter your height and weight",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }



    private void updatebmi(Double bmi, String keyId,View view) {
        //converting bmi to string
        String newbmi = String.format("%.2f",bmi);
        //putting value in a hashmap
        HashMap hashMap = new HashMap();
        hashMap.put("bmi",newbmi);

        //updating profile with value
        reference.child(keyId).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        stopAnim();
                        Snackbar snackbar = Snackbar.make(view,"Profile updated with new BMI",Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                });
    }



    public void dashboard(View view) {
        Intent intent = new Intent(bmi.this, UserDashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        startAnim();
        //fetching user info
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser != null){
            //get the email of the user
            email = currentuser.getEmail();
            //fetch current user infomation
            fetchUserInfo(email);
        }

    }

    //fetch user info method
    private void fetchUserInfo(String email) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.child("email").getValue().equals(email)){
                        keyId = ds.child("key").getValue(String.class);
                        stopAnim();
                    }else {
//                        Snackbar snackbar = Snackbar.make(bmiview,"An error occured fetching user information",Snackbar.LENGTH_LONG);
//                        snackbar.show();
                        stopAnim();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar snackbar = Snackbar.make(bmiview,"Error: "+error,Snackbar.LENGTH_LONG);
                snackbar.show();
                stopAnim();

            }
        });
    }

    private void startAnim() {
        bmiavi.smoothToShow();
    }

    private void stopAnim() {
        bmiavi.smoothToHide();
    }

    public void profile(View view) {
        Intent intent = new Intent(bmi.this,profile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }
}