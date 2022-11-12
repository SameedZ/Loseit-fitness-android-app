package com.example.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Exercise_Area extends AppCompatActivity {
    private TextView tvexercisename,tvcountdown;
    private ImageView next,previous,pause,backtoexercise;
    private CountDownTimer countDownTimer;
    private long starttime_in_millis = 6000;
    private long time_left = starttime_in_millis;
    private boolean timerunning;
    private MediaPlayer mediaPlayer;
    private boolean done =false;
    private List<exercise_class> exerciseClassList;
    private  int count =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise__area);

        tvexercisename = (TextView)findViewById(R.id.exerciseName);
        tvcountdown = (TextView)findViewById(R.id.tvcountdown);
        backtoexercise = (ImageView)findViewById(R.id.backtoexercise);
        next = (ImageView)findViewById(R.id.next);
        previous = (ImageView)findViewById(R.id.previous);
        pause = (ImageView)findViewById(R.id.pause);

        Bundle bundle = getIntent().getExtras();
        String cat = bundle.getString("cat");

        exerciseClassList = new ArrayList<>();


        //getting data from database
        Query query = FirebaseDatabase.getInstance().getReference("exercise").orderByChild("category").equalTo(cat);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                run(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backtoexercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("cat",cat);
                Intent intent = new Intent(Exercise_Area.this,UserExercise_Details.class);
                intent.putExtras(bundle1);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (done){
                    //check if size of list is greater than count
                    if (count<=exerciseClassList.size()){
                        //get next element
                        String name = exerciseClassList.get(count).getName();
                        tvexercisename.setText(name);
                        time_left = Long.parseLong(exerciseClassList.get(count).getDuration())*1000;
                        starttime_in_millis=time_left;
                        startTimer();
                    }else {
                        //show this is last element
                        Snackbar.make(v,"No more exercises",Snackbar.LENGTH_LONG).show();
                    }

                }else {
//                    count +=1;
//                    //get next element
//                    String name = exerciseClassList.get(count).getName();
//                    tvexercisename.setText(name);
//                    time_left = Long.parseLong(exerciseClassList.get(count).getDuration())*1000;
//                    starttime_in_millis=time_left;
//                    startTimer();
                }
            }
        });


        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerunning){
                    pause.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
                    countDownTimer.cancel();
                    timerunning = false;
                }else {
                    startTimer();
                }


            }
        });
    }

    private void run(DataSnapshot snapshot) {
        for (DataSnapshot ds:snapshot.getChildren()){
            exercise_class data = ds.getValue(exercise_class.class);
            exerciseClassList.add(data);
        }
        String name = exerciseClassList.get(count).getName();
        tvexercisename.setText(name);
        time_left = Long.parseLong(exerciseClassList.get(count).getDuration())*1000;
        starttime_in_millis=time_left;
        startTimer();
    }

    private void startTimer() {
        Log.d("alarm", "startTimer: On start");
        pause.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);
        countDownTimer = new CountDownTimer(time_left,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time_left = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                playSound();
                pause.setBackgroundResource(R.drawable.ic_baseline_play_circle_outline_24);
                timerunning=false;
                time_left = starttime_in_millis;
                count +=1;
                done = true;

            }
        }.start();

        timerunning=true;
    }

    private void playSound() {
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this,R.raw.alarmbeep);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }
        mediaPlayer.start();
    }

    private void stopPlayer(){
        if (mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlayer();
    }

    private void updateCountDownText() {
        int minutes = (int)(time_left / 1000) / 60;
        int seconds = (int)(time_left / 1000) % 60;

        String timeleftFormatted = String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        tvcountdown.setText(timeleftFormatted);
    }


}
