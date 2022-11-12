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

public class profile extends AppCompatActivity {
    private TextView username;
    private EditText fname,lname,phone;
    private Button update;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mdatabase;
    private DatabaseReference reference;

    private String email;
    private String keyId;
    private View content;
    private AVLoadingIndicatorView proavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //overridePendingTransition(R.anim.enter_from_right, R.anim.exit_through_right);


        username = (TextView)findViewById(R.id.username);
        fname = (EditText)findViewById(R.id.fname);
        lname = (EditText)findViewById(R.id.lname);
        phone = (EditText)findViewById(R.id.phone);
        update = (Button)findViewById(R.id.update);

        content = (View)findViewById(R.id.contnt);

        proavi = (AVLoadingIndicatorView)findViewById(R.id.proavi);


        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        reference = mdatabase.getReference("user");

        //setting onclick button for button
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting the data from the edit text
                String Fname = fname.getText().toString().trim();
                String Lname = lname.getText().toString().trim();
                String Phone = phone.getText().toString();

                //input validations
                if ( !TextUtils.isEmpty(Fname) && !TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Lname)){
                    //if edit texts are not empty
                    //convert age to in for another validation
                    //int newphone = Integer.parseInt(Phone);

                    //check if newage is not zero
                    if (TextUtils.getTrimmedLength(Phone) > 0 && TextUtils.getTrimmedLength(Phone) <=10){
                        //do user profile update here
                        startAnim();
                        updateprofile(Fname,Lname,Phone,keyId,v);
                    }else{
                        //if age is less than five
                        phone.setError("");
                        Snackbar snackbar = Snackbar.make(v,"Invalid phone number length",Snackbar.LENGTH_LONG);
                        snackbar.show();
                        phone.requestFocus();
                    }
                }else{
                    if (TextUtils.isEmpty(Fname)){
                        fname.setError("Field is empty");
                        fname.requestFocus();
                    }
                    if (TextUtils.isEmpty(Lname)){
                        lname.setError("Field is empty");
                        lname.requestFocus();
                    }
                    if (TextUtils.isEmpty(Phone)){
                        phone.setError("Field is empty");
                        phone.requestFocus();
                    }
                }
            }
        });
    }



    private void updateprofile(String fname,String lname, String phone, String keyId, View view) {
        startAnim();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fname",fname);
        hashMap.put("lname",lname);
        hashMap.put("phone",phone);
        reference.child(keyId).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(profile.this, "Profile updated", Toast.LENGTH_SHORT).show();

                Snackbar snackbar = Snackbar.make(view,"Profile updated",Snackbar.LENGTH_LONG);
                snackbar.show();
                stopAnim();

            }
        });
    }

    public void dashboard(View view) {
        Intent intent = new Intent(profile.this, UserDashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAnim();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser != null){
            //get the email of the user
            email = currentuser.getEmail();

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
                        //getting the values for the user
                        String firstname = ds.child("fname").getValue(String.class);
                        String lastname = ds.child("lname").getValue(String.class);
                        String myphone = ds.child("phone").getValue(String.class);
                        keyId = ds.child("key").getValue(String.class);

                        //checking if fields are empty or not
                        if ( !TextUtils.isEmpty(firstname)){
                            fname.setText(firstname);
                        }
                        if ( !TextUtils.isEmpty(myphone)){
                            phone.setText(myphone);
                        }
                        if ( !TextUtils.isEmpty(lastname)){
                            lname.setText(lastname);
                        }
                        stopAnim();

                    }else {
                        stopAnim();
//                        Snackbar snackbar = Snackbar.make(content,"An error occured fetching user information",Snackbar.LENGTH_LONG);
//                        snackbar.show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                stopAnim();
                Snackbar snackbar = Snackbar.make(content,""+error,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void startAnim() {
        proavi.smoothToShow();
    }
    private void stopAnim() {
        proavi.smoothToHide();
    }
}