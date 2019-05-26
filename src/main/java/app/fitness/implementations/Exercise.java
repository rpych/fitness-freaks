package app.fitness.implementations;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Integer rounds;
    private Integer repetitionsInOneRound;

    public Exercise(String name, Integer rounds, Integer repetitionsInOneRound) {
        this.name = name;
        this.rounds = rounds;
        this.repetitionsInOneRound = repetitionsInOneRound;
    }

    public Exercise(String name) {
        this.name = name;
    }

    public Exercise() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
