package app.fitness.implementations;

import javax.persistence.*;

@Entity
public class LoggedExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "primary_id", nullable = false)
    private Long primaryId;
    private Long id; //userId
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

    public Integer getAllRepetitions() {
        return allRepetitions;
    }

    public void setAllRepetitions(Integer allRepetitions) {
        this.allRepetitions = allRepetitions;
    }
}
