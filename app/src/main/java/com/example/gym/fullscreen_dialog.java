package com.example.gym;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

public class fullscreen_dialog extends DialogFragment {

    private String ename,edesc,edura,cat;
    private ImageView close,done;
    private EditText name,des,dura;
    private AVLoadingIndicatorView avi;

    static fullscreen_dialog newInstance(){
        return new fullscreen_dialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.FullscreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_exercise_dialog,container,false);

        Bundle bundle = getArguments();

        if (bundle !=null){
            cat = bundle.getString("category");
        }



        FirebaseDatabase database;
        DatabaseReference reference;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("exercise");

        //linking to xml
        close = view.findViewById(R.id.close);
        done = view.findViewById(R.id.Done);
        name = view.findViewById(R.id.name);
        des = view.findViewById(R.id.desc);
        dura = view.findViewById(R.id.duration);
        avi = view.findViewById(R.id.aviaddexercise);

        //setting onclick listerner for done
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting the input from the edit text fields
                ename = name.getText().toString();
                edesc = des.getText().toString();
                edura = dura.getText().toString();

                //checking if fields are not empty
                if (!TextUtils.isEmpty(ename) && !TextUtils.isEmpty(edesc) && !TextUtils.isEmpty(edura)){
                    startAnim();
                    //if none is empty then insert into database
                    String exerciseID = reference.push().getKey();

                    exercise_class  exerciseClass = new exercise_class(ename,edesc,edura,exerciseID,cat);
                    reference.child(exerciseID).setValue(exerciseClass)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    stopAnim();
                                    Snackbar snackbar = Snackbar.make(v,"Error: "+e,Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            })

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            stopAnim();
                           getDialog().dismiss();
                        }
                    });

                }
                else {
                    Snackbar snackbar = Snackbar.make(v,"Fill all fields",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });

        //setting onclick listernet for close
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    private void stopAnim() {
        avi.hide();
    }

    private void startAnim() {
        avi.smoothToShow();
    }
}
