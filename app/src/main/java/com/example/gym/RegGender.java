package com.example.gym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class RegGender extends AppCompatActivity {
    private Button btnchoice;
    private ImageView imgfemale,imgmale;
    private String key,gender;
    public boolean selected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_gender);

        btnchoice = (Button)findViewById(R.id.tochoice);
        imgfemale = (ImageView)findViewById(R.id.femalecheck);
        imgmale = (ImageView)findViewById(R.id.malecheck);

        //getting the data passed from the register activity
        Bundle data = getIntent().getExtras();
        if (data !=null){
            key = data.getString("key");
        }

        //setting an onclick listener for the button
        btnchoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected){
                    //getting the bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("key",key);
                    bundle.putString("gender",gender);
                    //go to choice intent
                    Intent intent = new Intent(RegGender.this, RegChoice.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
                }else {
                    Snackbar snackbar = Snackbar.make(v,"Select gender to continue",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void female(View view) {
        imgfemale.setVisibility(View.VISIBLE);
        imgmale.setVisibility(View.INVISIBLE);
        gender="female";
        selected=true;

    }

    public void male(View view) {
        imgmale.setVisibility(View.VISIBLE);
        imgfemale.setVisibility(View.INVISIBLE);
        gender="male";
        selected=true;
    }
}