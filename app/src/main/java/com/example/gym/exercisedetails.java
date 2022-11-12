package com.example.gym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class exercisedetails extends AppCompatActivity implements ExerciseAdapter.SelectdExercise {
    private FloatingActionButton fab;
    View view;
    TextView tvdisplay;
    private RecyclerView recyclerView;
    List<exercise_class> exerciseClassList;
    DatabaseReference reference;

    ExerciseAdapter exerciseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisedetails);

        //getting the extra from previous intent
        Bundle bundle = getIntent().getExtras();

        view = (View)findViewById(R.id.layview);
        tvdisplay = (TextView)findViewById(R.id.exercisedisplay);
        tvdisplay.setText(bundle.getString("category"));

        fab = (FloatingActionButton)findViewById(R.id.fab);
        recyclerView = (RecyclerView)findViewById(R.id.rcvexercise);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        exerciseClassList = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference("exercise").orderByChild("category").equalTo(bundle.getString("category"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                run(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //hiding recyclerview on scroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = fullscreen_dialog.newInstance();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(),"tag");
            }
        });
    }

    private void run(DataSnapshot snapshot) {
        exerciseClassList.clear();
        for (DataSnapshot ds:snapshot.getChildren()){
            exercise_class data = ds.getValue(exercise_class.class);
            exerciseClassList.add(data);
        }

        if (exerciseClassList.size()==0){
            Snackbar snackbar = Snackbar.make(view,"No exercise added",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        try {
            //where we add data to adapter

            exerciseAdapter = new ExerciseAdapter(exerciseClassList,this);
            recyclerView.setAdapter(exerciseAdapter);
        }catch (Exception e){
            Snackbar snackbar = Snackbar.make(view,"Error: "+e,Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public void backtoexercise(View view) {
        Intent intent = new Intent(exercisedetails.this,exercise.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left,R.anim.exit_through_left);
        finish();
    }

    @Override
    public void selectdExercise(exercise_class exerciseClass) {
//        String name = exercise_class.getName();
//        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(exercisedetails.this);
//        bottomSheetDialog.setContentView(R.layout.edit_exercise_bottomsheet_layout);
//        bottomSheetDialog.setCanceledOnTouchOutside(false);
//
//        //linking to xml
//        TextView tvdisplay = bottomSheetDialog.findViewById(R.id.exercisedisplay);
//        tvdisplay.setText(name);
//
//
//        bottomSheetDialog.show();
        Bundle bundle = new Bundle();
        bundle.putString("name",exerciseClass.getName());
        bundle.putString("dura",exerciseClass.getDuration());
        bundle.putString("description",exerciseClass.getDescription());
        bundle.putString("key",exerciseClass.getExerciseID());
        //Toast.makeText(this, ""+exerciseClass.getName(), Toast.LENGTH_SHORT).show();

        DialogFragment dialogFragmentBS = EditExercise_bottomsheet_dialog.newInstance();
        dialogFragmentBS.setArguments(bundle);
        dialogFragmentBS.show(getSupportFragmentManager(),"tagg");
    }
}