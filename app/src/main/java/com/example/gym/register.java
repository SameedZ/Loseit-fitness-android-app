package com.example.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

public class register extends AppCompatActivity {

    private EditText email,password,cpassword;
    private Button register;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    private User newuser;

//    //declaring global variables
    private String  useremail;
    private String userpassword;
    private String retypepassword;
    private String phone ="";
    private String fname ="";
    private String lname ="";
    private String bmi ="";
    private String level="";
    private String choice="";
    private String gender = "";
    private AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //overridePendingTransition(R.anim.enter, R.anim.exit);

        //for loading
        avi = (AVLoadingIndicatorView)findViewById(R.id.registeravi);

        //linking to xml
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        cpassword = (EditText)findViewById(R.id.cpassword);
        register = (Button)findViewById(R.id.btnregister);

        //for firebaseauth
        mAuth = FirebaseAuth.getInstance();

        //for firebaseDatabase
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("user");

        stopAnim();

        //onclick method for the register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user input validations
                useremail = email.getText().toString().trim(); //getting the email
                userpassword = password.getText().toString().trim(); //getting the password
                retypepassword = cpassword.getText().toString().trim(); //getting the cpassword

                //check if fields are not empty
                if (!TextUtils.isEmpty(useremail) && !TextUtils.isEmpty(userpassword)){

                    //checking if email is valid
                    if (Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
                        //if email is valid, check if password match
                        if (userpassword.length()>=6){
                            if (userpassword.equals(retypepassword)){
                                startAnim();

                                //check if email already exist in database
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot ds: snapshot.getChildren()){
                                            if (ds.child("email").getValue().equals(useremail)){
                                                Snackbar snackbar = Snackbar.make(v,"Email exist",Snackbar.LENGTH_LONG);
                                                snackbar.show();
                                                email.requestFocus();
                                                stopAnim();
                                            }else {
                                                registerUser(useremail,userpassword,v);//method to register user, declared below
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        stopAnim();
                                        Snackbar snackbar = Snackbar.make(v,""+error,Snackbar.LENGTH_LONG);
                                        snackbar.show();

                                    }
                                });

                            }else {
                                //if password do not match
    //                            Toast.makeText(register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                                //refocus on password field
    //                            password.setError("Passwords do not match");
    //                            cpassword.setError("Passwords do not match");
                                stopAnim();
                                Snackbar snackbar = Snackbar.make(v,"Passwords do not match",Snackbar.LENGTH_LONG);
                                snackbar.show();
                                password.requestFocus();
                            }
                        }else {
                            stopAnim();
                            Snackbar snackbar = Snackbar.make(v,"Password should be at least 6 characters",Snackbar.LENGTH_LONG);
                            snackbar.show();
                            password.requestFocus();
                        }

                    }else {
                        //if email is not valid
//                        Toast.makeText(register.this, "Email must take the form example@example.com", Toast.LENGTH_SHORT).show();
                        //refocus on email field
//                        email.setError("Enter valid email");
                        stopAnim();
                        Snackbar snackbar = Snackbar.make(v,"Enter valid email",Snackbar.LENGTH_LONG);
                        snackbar.show();
                        email.requestFocus();
                    }

                }else {
                    //if user submits empty fields
//                    Toast.makeText(register.this, "Enter Email and Password", Toast.LENGTH_SHORT).show();
                    //requesting focus
//                    email.setError("Enter email");
//                    password.setError("Enter password");
                    stopAnim();
                    Snackbar snackbar = Snackbar.make(v,"Enter email and password",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    email.requestFocus();
                }

            }
        });
    }


    //method to register user
    private void registerUser(String email, String password,View v) {


        mAuth.createUserWithEmailAndPassword(email,password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //if task is successful get user information
                    //Toast.makeText(register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }else{
                    task.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            stopAnim();
                            Snackbar snackbar = Snackbar.make(v,"Error: "+e,Snackbar.LENGTH_LONG);
                            snackbar.show();
                            Log.d("fail", "onFailure: "+e);
                        }
                    });
//                    Toast.makeText(register.this, "User not registered", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    //update UI method
    private void updateUI(FirebaseUser user) {

        //store user info with id
        String keyId = reference.push().getKey();

        //populate the user class
        newuser = new User(useremail,userpassword,fname,lname,bmi,phone,keyId,level,choice,gender);

        reference.child(keyId).setValue(newuser);
        //reference.setValue("shoelace");
        //go to dashboard if all is successful
        stopAnim();
        Intent intent = new Intent(register.this, RegGender.class);
        intent.putExtra("key",keyId);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }

    private void stopAnim() {
        avi.smoothToHide();
    }

    private void startAnim() {
        avi.smoothToShow();
    }



    //funtion for move to login page
    public void tologin(View view) {
        Intent intent = new Intent(register.this, login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();
    }
}