package com.example.gym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class exercise extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
    }

    public void dashboard(View view) {
        Intent intent = new Intent(exercise.this,AdminDashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();
    }

    public void toexercisedetailsBeginner(View view) {
        Intent intent = new Intent(exercise.this,exercisedetails.class);
        intent.putExtra("category","loose belly fat");
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }

    public void toexercisedetailsInter(View view) {
        Intent intent = new Intent(exercise.this,exercisedetails.class);
        intent.putExtra("category","rock hard abs");
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }

    public void toexercisedetailsAdvanced(View view) {
        Intent intent = new Intent(exercise.this,exercisedetails.class);
        intent.putExtra("category","six pack abs");
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }

}