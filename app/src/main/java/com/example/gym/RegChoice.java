package com.example.gym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

public class RegChoice extends AppCompatActivity {
    private Button btntoexercise;
    private ImageView abs,bfat;
    private String exercisechoice;
    private boolean selected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_choice);

        btntoexercise = (Button)findViewById(R.id.toexercise);
        abs = (ImageView)findViewById(R.id.abscheck);
        bfat = (ImageView)findViewById(R.id.bfatcheck);

        //getting the data from previous intent
        Bundle data = getIntent().getExtras();

        btntoexercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key",data.getString("key"));
                bundle.putString("gender",data.getString("gender"));
                bundle.putString("choice",exercisechoice);
                if (selected){
                    //go to exercise intent
                    Intent intent = new Intent(RegChoice.this,RegExercise.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);

                }else {
                    Snackbar snackbar = Snackbar.make(v,"Choose a choice",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });
    }

    public void abs(View view) {
        abs.setVisibility(View.VISIBLE);
        bfat.setVisibility(View.INVISIBLE);
        exercisechoice="Abs";
        selected=true;
    }

    public void bfat(View view) {
        abs.setVisibility(View.INVISIBLE);
        bfat.setVisibility(View.VISIBLE);
        exercisechoice="Belly Fat";
        selected=true;
    }
}