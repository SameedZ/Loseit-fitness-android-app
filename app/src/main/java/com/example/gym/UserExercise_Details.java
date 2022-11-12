package com.example.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserExercise_Details extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    List<exercise_class> exerciseClassList;
    RecyclerView recyclerView;
    TextView tvdisplay;
    ExtendedFloatingActionButton extnfab;
    View view;
    UserExercise_Adapter userExerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_exercise__details);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container),true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.cordlayout);
        view = (View)findViewById(R.id.laview);
        extnfab = (ExtendedFloatingActionButton)findViewById(R.id.etndfab);
        tvdisplay = (TextView)findViewById(R.id.userexercisedisplay);
        recyclerView = (RecyclerView)findViewById(R.id.rcvuserexercise);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        exerciseClassList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        String cat = bundle.getString("cat");

        tvdisplay.setText(cat);

        switch (cat){
            case "loose belly fat":
                coordinatorLayout.setBackgroundResource(R.drawable.bodyy);
                break;
            case "rock hard abs":
                coordinatorLayout.setBackgroundResource(R.drawable.bodyyyyyyyyy);
                break;
            case "six pack abs":
                coordinatorLayout.setBackgroundResource(R.drawable.bodyyyyyyy);
                break;
        }

        extnfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("cat",cat);
                Intent intent = new Intent(UserExercise_Details.this,Exercise_Area.class);
                intent.putExtras(bundle1);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right,R.anim.exit_through_right);
            }
        });

        //hiding recyclerview on scroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && extnfab.getVisibility() == View.VISIBLE) {
                    extnfab.hide();
                } else if (dy < 0 && extnfab.getVisibility() != View.VISIBLE) {
                    extnfab.show();
                }
            }
        });

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
    }

    private void run(DataSnapshot snapshot) {
        for (DataSnapshot ds:snapshot.getChildren()){
            exercise_class data = ds.getValue(exercise_class.class);
            exerciseClassList.add(data);
        }
        if (exerciseClassList.size()==0){
            Snackbar snackbar = Snackbar.make(view,"No exercise added",Snackbar.LENGTH_LONG);
            snackbar.show();
            extnfab.hide();
        }
        try {
            //where we add data to adapter
            userExerciseAdapter = new UserExercise_Adapter(exerciseClassList);
            recyclerView.setAdapter(userExerciseAdapter);
        }catch (Exception e){
            Snackbar snackbar = Snackbar.make(view,"Error: "+e,Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

//    public void todashboard(View view) {
//        Intent intent = new Intent(UserExercise_Details.this,UserExercise.class);
//        startActivity(intent);
////        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
//        finish();
//    }
}