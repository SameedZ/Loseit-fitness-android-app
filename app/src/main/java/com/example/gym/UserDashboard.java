package com.example.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserDashboard extends AppCompatActivity {
   private FirebaseAuth mAuth;
   private String email,key;
   private TextView username,tel,mybmi;
   private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdashboard);

        //overridePendingTransition(R.anim.enter_from_right, R.anim.exit_through_right);

        username = (TextView)findViewById(R.id.username);
        tel = (TextView)findViewById(R.id.tel);
        mybmi = (TextView)findViewById(R.id.mybmi);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentuser = mAuth.getCurrentUser();
        if (currentuser != null){
            email = currentuser.getEmail();
           // key = currentuser.getIdToken();
        }


        Query query = FirebaseDatabase.getInstance().getReference("user").orderByChild("email").equalTo(email);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    user = ds.getValue(User.class);
                }
                //key = user.getKey();

                if (user !=null){
                    key=user.getKey();
                    //setting user first name and last name if available else set email
                    if (TextUtils.isEmpty(user.getFname()) && TextUtils.isEmpty(user.getLname())){
                        username.setText(email);
                    }else {
                        username.setText(user.getFname()+" "+user.getLname());
                    }
                    //setting user bmi if available
                    if ( !TextUtils.isEmpty(user.getBmi())){
                        mybmi.setText(user.getBmi()+" kg/m2");
                    }
                    //setting the phone number
                    if ( !TextUtils.isEmpty(user.getPhone())){
                        tel.setText(user.getPhone());
                    }

                }else {
                    Log.d("user", "onDataChange: user is null "+user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void logoutUser(View view) {
        //logout user
        FirebaseAuth.getInstance().signOut();

        //go back to login activity
        Intent intent = new Intent(UserDashboard.this, login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();
    }

    public void bmi(View view) {
        Intent intent = new Intent(UserDashboard.this,bmi.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }

    public void profile(View view) {
        Intent intent = new Intent(UserDashboard.this,profile.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }

    public void topayment(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("email",email);
        Intent intent = new Intent(UserDashboard.this,payment.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }

    public void exercise(View view) {
        Intent intent = new Intent(UserDashboard.this,UserExercise.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
        finish();
    }
}