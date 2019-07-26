package app.fitness.implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Component
public class ExercisesSolver {
    private List<String> exerciseNames;
    private Map<String, Integer> roundsUpperBoundPerExercise;
    private Map<String, Integer> repetitionsUpperBoundPerExercise;
    public final static Integer WEAK_SHAPE = 25;
    public final static Integer MED_SHAPE = 50;
    public final static Integer GOOD_SHAPE = 75;

    public ExercisesSolver() {
        this.exerciseNames = Arrays.asList("Pompki", "Brzuszki", "Deska", "Podciąganie na drążku", "Martwy ciąg");
        this.roundsUpperBoundPerExercise = new HashMap<>();
        this.repetitionsUpperBoundPerExercise = new HashMap<>();
        prepareExerciseSolver();
    }

    public void prepareExerciseSolver(){
        roundsUpperBoundPerExercise.put("Pompki", 10);
        roundsUpperBoundPerExercise.put("Brzuszki", 10);
        roundsUpperBoundPerExercise.put("Deska", 10);
        roundsUpperBoundPerExercise.put("Podciąganie na drążku", 10);
        roundsUpperBoundPerExercise.put("Martwy ciąg", 7);

        repetitionsUpperBoundPerExercise.put("Pompki", 100);
        repetitionsUpperBoundPerExercise.put("Brzuszki", 100);
        repetitionsUpperBoundPerExercise.put("Deska", 6); //minutes
        repetitionsUpperBoundPerExercise.put("Podciąganie na drążku", 30);
        repetitionsUpperBoundPerExercise.put("Martwy ciąg", 8);
    }

    public DailyExercise getCustomExercise(String date, String exerName, Integer relShape){
        Integer maxRounds = roundsUpperBoundPerExercise.getOrDefault(exerName, 6);
        Integer maxRepetitions = repetitionsUpperBoundPerExercise.getOrDefault(exerName, 7);
        if(relShape < WEAK_SHAPE){
            maxRounds /= 6;
            maxRepetitions /= 6;
        }
        else if(relShape < MED_SHAPE){
            maxRounds /= 5;
            maxRepetitions /= 5;
        }
        else if(relShape <GOOD_SHAPE){
            maxRounds /= 4;
            maxRepetitions /= 4;
        }
        else{
            maxRounds /= 3;
            maxRepetitions /= 3;
        }
        DailyExercise ex = new DailyExercise(date, exerName, maxRounds, maxRepetitions);
        return ex;
    }

    public List<String> getExerciseNames() {
        return exerciseNames;
    }

    public void setExerciseNames(List<String> exerciseNames) {
        this.exerciseNames = exerciseNames;
    }

    public Map<String, Integer> getRoundsUpperBoundPerExercise() {
        return roundsUpperBoundPerExercise;
    }

    public void setRoundsUpperBoundPerExercise(Map<String, Integer> roundsUpperBoundPerExercise) {
        this.roundsUpperBoundPerExercise = roundsUpperBoundPerExercise;
    }

    public Map<String, Integer> getRepetitionsUpperBoundPerExercise() {
        return repetitionsUpperBoundPerExercise;
    }

    public void setRepetitionsUpperBoundPerExercise(Map<String, Integer> repetitionsUpperBoundPerExercise) {
        this.repetitionsUpperBoundPerExercise = repetitionsUpperBoundPerExercise;
    }
}
