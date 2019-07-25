package app.fitness.implementations;

public class LoggedExercise {

    private String date;
    private String name;
    private Integer allRepetitions;

    public LoggedExercise(String date, String name, Integer allRepetitions) {
        this.date = date;
        this.name = name;
        this.allRepetitions = allRepetitions;
    }

    public LoggedExercise() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAllRepetitions() {
        return allRepetitions;
    }

    public void setAllRepetitions(Integer allRepetitions) {
        this.allRepetitions = allRepetitions;
    }
}
