package app.fitness.implementations;

import javax.persistence.*;

@Entity
public class DailyExercise { //maybe it swap class Exercise

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "primary_id", nullable = false)
    private Long primaryId;
    private Long id; //clientId
    private String date;
    private String name;
    private Integer rounds;
    private Integer repetitionsInOneRound;

    public DailyExercise(String date, String name, Integer rounds, Integer repetitionsInOneRound) {
        this.date = date;
        this.name = name;
        this.rounds = rounds;
        this.repetitionsInOneRound = repetitionsInOneRound;
    }

    public DailyExercise() {
    }

    public Long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(Long primaryId) {
        this.primaryId = primaryId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getRounds() {
        return rounds;
    }

    public void setRounds(Integer rounds) {
        this.rounds = rounds;
    }

    public Integer getRepetitionsInOneRound() {
        return repetitionsInOneRound;
    }

    public void setRepetitionsInOneRound(Integer repetitionsInOneRound) {
        this.repetitionsInOneRound = repetitionsInOneRound;
    }
}
