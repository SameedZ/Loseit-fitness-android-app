package com.example.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wang.avi.AVLoadingIndicatorView;

public class login extends AppCompatActivity {
    private EditText email, password;
    private Button login;
    private AVLoadingIndicatorView aviview;
    private View loginview;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginview = (View)findViewById(R.id.loginview);

        FirebaseApp.initializeApp(this);

        //initializing variables
        email = (EditText)findViewById(R.id.lemail);
        password = (EditText)findViewById(R.id.lpassword);
        login = (Button)findViewById(R.id.login);
        aviview = (AVLoadingIndicatorView) findViewById(R.id.avi);

        //for firebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //checking for internet connectivity
        ConnectivityStatus connectivityStatus = new ConnectivityStatus(getApplicationContext());
        boolean IsConnected = connectivityStatus.isConnected();

        if (IsConnected){
            //do nothing
//            Snackbar snackbar = Snackbar.make(loginview,"You are connected to the internet",Snackbar.LENGTH_LONG);
//            snackbar.show();


        }else {
            Snackbar snackbar = Snackbar.make(loginview,"You are not connected to the internet",Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        stopAnim();

        //setting an onclick listener for the login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the values from the email and password fields
                String useremail = email.getText().toString();
                String userpassword = password.getText().toString();

                //input validations
                if (!TextUtils.isEmpty(useremail) && !TextUtils.isEmpty(userpassword)){
                    //if email and password fields are not empty, check if user entered email
                    if(Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
                        //if what is entered matches an email address, login user in
                        //loading
                        startAnim();
                        loginUser(useremail,userpassword,v);
                    }else{
                        email.setError("");
                        Snackbar snackbar = Snackbar.make(v,"Enter a valid email",Snackbar.LENGTH_LONG);
                        snackbar.show();
                        email.requestFocus();
                    }
                }else{
                    email.setError("");
                    Snackbar snackbar = Snackbar.make(v,"Enter email and password",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    password.setError("");
                    email.requestFocus();
                }

            }
        });
    }

    private void startAnim() {
        aviview.smoothToShow();
    }

    //login method
    private void loginUser(String useremail, String userpassword,View v) {
        //log user in
        mAuth.signInWithEmailAndPassword(useremail,userpassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            updateUI(currentUser);

                        }else {
                            stopAnim();
                            Snackbar snackbar = Snackbar.make(v,"Check credentials and try again.",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }

                    }
                });

    }

    //updateUI method definition
    private void updateUI(FirebaseUser currentUser) {
        stopAnim();
        //go to dashboard
        Intent intent = new Intent(login.this, UserDashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }

    private void stopAnim() {
        aviview.smoothToHide();
    }

    //check if user is logged in on app start
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser != null){
            String email = currentuser.getEmail();
            if (email.equals("admin@gmail.com")){
                mAuth.signOut();
                Snackbar snackbar = Snackbar.make(loginview,"Admin logged out",Snackbar.LENGTH_LONG);
                snackbar.show();
            }else {
                Intent intent = new Intent(login.this, UserDashboard.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
                finish();
            }

        }
    }

    //method to go to register form
    public void toregister(View view) {
        Intent intent = new Intent(login.this, register.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }

    public void toadminlogin(View view) {
        Intent intent = new Intent(login.this,AdminLogin.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }
}