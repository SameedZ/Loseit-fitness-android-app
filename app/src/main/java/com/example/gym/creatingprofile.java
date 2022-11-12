package com.example.gym;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;

public class creatingprofile extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private AVLoadingIndicatorView profileavi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatingprofile);

        profileavi = (AVLoadingIndicatorView)findViewById(R.id.profileavi);

        startAnim();
        //getting the data from the previous intent
        Bundle data = getIntent().getExtras();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("user");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("choice",data.getString("choice"));
        hashMap.put("level",data.getString("level"));
        hashMap.put("gender",data.getString("gender"));

        reference.child(data.getString("key")).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //if successful go to dashboard
                        stopAnim();
                        Intent intent = new Intent(creatingprofile.this,UserDashboard.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
                        finish();

                    }
                });

    }

    private void stopAnim() {
        profileavi.hide();
    }

    private void startAnim() {
        profileavi.smoothToShow();
    }
}