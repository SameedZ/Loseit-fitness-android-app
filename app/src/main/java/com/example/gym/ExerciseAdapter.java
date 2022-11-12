package com.example.gym;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseAdapterVH> {
    private List<exercise_class> exerciseClassList;
    private Context context;
    private SelectdExercise selectdExercise;

    public ExerciseAdapter(List<exercise_class> exerciseClassList, SelectdExercise selectdExercise) {
        this.exerciseClassList = exerciseClassList;
        this.selectdExercise = selectdExercise;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ExerciseAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ExerciseAdapterVH(LayoutInflater.from(context).inflate(R.layout.exercise_list_layout,null));
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseAdapter.ExerciseAdapterVH holder, int position) {
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

    public interface SelectdExercise{
        void selectdExercise(exercise_class exerciseClass);
    }

    public class ExerciseAdapterVH extends RecyclerView.ViewHolder{
        TextView ename,eduration;
        public ExerciseAdapterVH(@NonNull View itemView) {
            super(itemView);

            ename = itemView.findViewById(R.id.exercisename);
            eduration = itemView.findViewById(R.id.dura);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectdExercise.selectdExercise(exerciseClassList.get(getAdapterPosition()));
                }
            });
        }
    }
}
