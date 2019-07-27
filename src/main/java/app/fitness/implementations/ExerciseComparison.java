package app.fitness.implementations;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ExerciseComparison {

    private String name;
    private String date;
    private Integer loggedRepetitions;
    private Integer assumedRepetitions;

    public ExerciseComparison(String name, String date, Integer loggedRepetitions, Integer assumedRepetitions) {
        this.name = name;
        this.date = date;
        this.loggedRepetitions = loggedRepetitions;
        this.assumedRepetitions = assumedRepetitions;
    }

    public ExerciseComparison(String name) {
        this.name = name;
    }

    public ExerciseComparison() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getLoggedRepetitions() {
        return loggedRepetitions;
    }

    public void setLoggedRepetitions(Integer loggedRepetitions) {
        this.loggedRepetitions = loggedRepetitions;
    }

    public Integer getAssumedRepetitions() {
        return assumedRepetitions;
    }

    public void setAssumedRepetitions(Integer assumedRepetitions) {
        this.assumedRepetitions = assumedRepetitions;
    }
}
