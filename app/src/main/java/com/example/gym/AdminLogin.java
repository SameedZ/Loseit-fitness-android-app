package com.example.gym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.wang.avi.AVLoadingIndicatorView;

public class AdminLogin extends AppCompatActivity {
    private EditText pin;
    private Button admincheck;
    private String Adminpin="1234";
    private String email="admin@gmail.com";
    private String password = "password";
    private AVLoadingIndicatorView adminloginavi;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        //linking to xml
        pin = (EditText)findViewById(R.id.pinnum);
        admincheck = (Button) findViewById(R.id.admincheck);
        adminloginavi = (AVLoadingIndicatorView)findViewById(R.id.adminloginavi);

        //for firebaselogin
        mAuth = FirebaseAuth.getInstance();

        //onclick listener for button
        admincheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting the pin entered by the user
                String inputpin = pin.getText().toString();

                //validations
                if (inputpin.equals(Adminpin)){
                    //start loading animation
                    startAnim();
                    //do login here
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    //stop animation
                                    stopAnim();
                                    //go to admin dashboard
                                    Intent intent = new Intent(AdminLogin.this,AdminDashboard.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
                                    finish();

                                }
                            });
                }else{
                    Snackbar snackbar = Snackbar.make(v,"Incorrect pin!",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    private void stopAnim() {
        adminloginavi.smoothToHide();
    }

    private void startAnim() {
        adminloginavi.smoothToShow();
    }

    public void backtologin(View view) {
        Intent intent = new Intent(AdminLogin.this,login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();
    }
}