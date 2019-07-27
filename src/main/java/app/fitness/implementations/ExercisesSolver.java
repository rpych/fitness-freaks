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
        this.exerciseNames = Arrays.asList("Pompki", "Brzuszki", "Biceps", "Podciaganie na drazku", "Martwy ciag");
        this.roundsUpperBoundPerExercise = new HashMap<>();
        this.repetitionsUpperBoundPerExercise = new HashMap<>();
        prepareExerciseSolver();
    }

    public void prepareExerciseSolver(){
        roundsUpperBoundPerExercise.put("Pompki", 10);
        roundsUpperBoundPerExercise.put("Brzuszki", 10);
        roundsUpperBoundPerExercise.put("Biceps", 10);
        roundsUpperBoundPerExercise.put("Podciąganie na drążku", 10);
        roundsUpperBoundPerExercise.put("Martwy ciąg", 7);

        repetitionsUpperBoundPerExercise.put("Pompki", 100);
        repetitionsUpperBoundPerExercise.put("Brzuszki", 100);
        repetitionsUpperBoundPerExercise.put("Biceps", 50);
        repetitionsUpperBoundPerExercise.put("Podciąganie na drążku", 30);
        repetitionsUpperBoundPerExercise.put("Martwy ciąg", 8);
    }

    public DailyExercise getCustomExercise(Long id, String date, String exerName, Integer relShape){
        int index;
        Integer maxRounds = roundsUpperBoundPerExercise.getOrDefault(exerName, 6);
        Integer maxRepetitions = repetitionsUpperBoundPerExercise.getOrDefault(exerName, 7);
        if(relShape < WEAK_SHAPE){
            index = (int)(Math.random() * 3 + 4);
            maxRounds /= index;
            maxRepetitions /= index;
        }
        else if(relShape < MED_SHAPE){
            index = (int)(Math.random() * 2 + 4);
            maxRounds /= index;
            maxRepetitions /= index;
        }
        else if(relShape <GOOD_SHAPE){
            index = (int)(Math.random() * 2 + 3);
            maxRounds /= index;
            maxRepetitions /= index;
        }
        else{
            index = 3;
            maxRounds /= index;
            maxRepetitions /= index;
        }
        DailyExercise ex = new DailyExercise(id, date, exerName, maxRounds, maxRepetitions);
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
