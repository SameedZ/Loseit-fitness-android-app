package com.example.gym;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;

public class UserExercise extends AppCompatActivity {
    ImageView imageView,absimage,siximage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_exercise);
        imageView = (ImageView)findViewById(R.id.fatimage);
        absimage = (ImageView)findViewById(R.id.absimage);
        siximage = (ImageView)findViewById(R.id.siximage);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container),true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    public void dashboard(View view) {
        Intent intent = new Intent(UserExercise.this,UserDashboard.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();
    }

    public void bellyfat(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("cat","loose belly fat");

        Intent intent = new Intent(UserExercise.this,UserExercise_Details.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(UserExercise.this,imageView, ViewCompat.getTransitionName(imageView));
        intent.putExtras(bundle);
        startActivity(intent,optionsCompat.toBundle());

    }

    public void hardabs(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("cat","rock hard abs");

        Intent intent = new Intent(UserExercise.this,UserExercise_Details.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(UserExercise.this,absimage,ViewCompat.getTransitionName(absimage));
        intent.putExtras(bundle);
        startActivity(intent,optionsCompat.toBundle());
    }

    public void packabs(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("cat","six pack abs");

        Intent intent = new Intent(UserExercise.this,UserExercise_Details.class);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(UserExercise.this,siximage,ViewCompat.getTransitionName(siximage));
        intent.putExtras(bundle);
        startActivity(intent,optionsCompat.toBundle());
    }
}