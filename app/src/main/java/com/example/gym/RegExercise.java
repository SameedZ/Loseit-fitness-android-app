package com.example.gym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

public class RegExercise extends AppCompatActivity {
    private Button done;
    private String level;
    private ImageView bcheck,intercheck,advancheck;
    boolean selected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_exercise);

        done = (Button)findViewById(R.id.done);
        bcheck = (ImageView)findViewById(R.id.beginnercheck);
        intercheck = (ImageView)findViewById(R.id.intercheck);
        advancheck = (ImageView)findViewById(R.id.advancecheck);

        //getting the data from previous intent
        Bundle data = getIntent().getExtras();

        //setting onclick listerner for button done
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected){
                    Bundle bundle = new Bundle();
                    bundle.putString("key",data.getString("key"));
                    bundle.putString("gender",data.getString("gender"));
                    bundle.putString("choice",data.getString("choice"));
                    bundle.putString("level",level);

                    Intent intent = new Intent(RegExercise.this,creatingprofile.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);

                }else {
                    Snackbar snackbar = Snackbar.make(v,"Choose your choice",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });
    }

    public void beginner(View view) {
        level = "Beginner";
        bcheck.setVisibility(View.VISIBLE);
        intercheck.setVisibility(View.INVISIBLE);
        advancheck.setVisibility(View.INVISIBLE);
        selected=true;
    }

    public void inter(View view) {
        level = "Intermediate";
        intercheck.setVisibility(View.VISIBLE);
        bcheck.setVisibility(View.INVISIBLE);
        advancheck.setVisibility(View.INVISIBLE);
        selected=true;
    }

    public void advanced(View view) {
        level = "Advanced";
        advancheck.setVisibility(View.VISIBLE);
        bcheck.setVisibility(View.INVISIBLE);
        intercheck.setVisibility(View.INVISIBLE);
        selected=true;
    }
}