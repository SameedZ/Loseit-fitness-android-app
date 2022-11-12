package com.example.gym;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;

public class EditExercise_bottomsheet_dialog  extends BottomSheetDialogFragment {
    private String name,key,duration,description;
    private TextView tvname,update,delete;
    private EditText ename,edesc,eduration;
    private AVLoadingIndicatorView avi;
    private DatabaseReference reference;

    static EditExercise_bottomsheet_dialog newInstance(){
        return new EditExercise_bottomsheet_dialog();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_exercise_bottomsheet_layout,container,false);

        Bundle bundle = getArguments();

        if (bundle !=null){
            name = bundle.getString("name");
            key = bundle.getString("key");
            duration = bundle.getString("dura");
            description = bundle.getString("description");

        }
        ename = view.findViewById(R.id.editname);
        edesc = view.findViewById(R.id.editdesc);
        eduration = view.findViewById(R.id.editduration);
        tvname = view.findViewById(R.id.exercisenamedisplay);
        avi = view.findViewById(R.id.avieditexercise);
        update = view.findViewById(R.id.editupdate);
        delete = view.findViewById(R.id.editdelete);
        reference = FirebaseDatabase.getInstance().getReference("exercise");

        tvname.setText(name);
        ename.setText(name);
        edesc.setText(description);
        eduration.setText(duration);

        //updating the exercise records
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
                //get the input
                String newename = ename.getText().toString();
                String newdura = eduration.getText().toString();
                String newdesc = eduration.getText().toString();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name",newename);
                hashMap.put("description",newdesc);
                hashMap.put("duration",newdura);

                reference.child(key).updateChildren(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                stopAnim();
                                Snackbar snackbar = Snackbar.make(view,"update successfull",Snackbar.LENGTH_LONG);
                                snackbar.show();
                                getDialog().dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                stopAnim();
                                Snackbar snackbar = Snackbar.make(view,"Error: "+e,Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        });
            }
        });


        //setting onclick listener for delete
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
                reference.child(key).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                stopAnim();
                                getDialog().dismiss();
                            }
                        })

                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                stopAnim();
                                Toast.makeText(getContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
                            }
                        });
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
