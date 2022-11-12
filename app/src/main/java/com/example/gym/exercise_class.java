package com.example.gym;

import java.io.Serializable;

public class exercise_class implements Serializable {
    private String name;
    private String description;
    private String duration;
    private String exerciseID;
    private String category;

    public exercise_class() {
    }

    public exercise_class(String name, String description, String duration, String exerciseID, String category) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.exerciseID = exerciseID;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(String exerciseID) {
        this.exerciseID = exerciseID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
