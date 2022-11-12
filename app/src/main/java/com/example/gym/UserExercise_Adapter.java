package com.example.gym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserExercise_Adapter extends RecyclerView.Adapter<UserExercise_Adapter.UserExercise_AdapterVH> {
    private List<exercise_class> exerciseClassList;
    private Context context;

    public UserExercise_Adapter(List<exercise_class> exerciseClassList) {
        this.exerciseClassList = exerciseClassList;
    }

    @NonNull
    @Override
    public UserExercise_Adapter.UserExercise_AdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new UserExercise_AdapterVH(LayoutInflater.from(context).inflate(R.layout.exercise_list_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull UserExercise_Adapter.UserExercise_AdapterVH holder, int position) {
        exercise_class exerciseClass = exerciseClassList.get(position);
        String exercisename = exerciseClass.getName();
        String exerciseduration = exerciseClass.getDuration();

        holder.ename.setText(exercisename);
        holder.eduration.setText(exerciseduration+" s");
    }

    @Override
    public int getItemCount() {
        return exerciseClassList.size();
    }

    public class UserExercise_AdapterVH extends RecyclerView.ViewHolder {
        TextView ename,eduration;
        public UserExercise_AdapterVH(@NonNull View itemView) {
            super(itemView);

            ename = itemView.findViewById(R.id.exercisename);
            eduration = itemView.findViewById(R.id.dura);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }
    }
}
