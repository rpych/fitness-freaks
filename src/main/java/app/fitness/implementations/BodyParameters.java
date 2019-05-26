package app.fitness.implementations;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BodyParameters {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double weight;
    private Double height;
    private Integer relativeShape; //in percents

    public BodyParameters(Double weight, Double height, Integer relativeShape) {
        this.weight = weight;
        this.height = height;
        this.relativeShape = relativeShape;
    }

    public BodyParameters() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Integer getRelativeShape() {
        return relativeShape;
    }

    public void setRelativeShape(Integer relativeShape) {
        this.relativeShape = relativeShape;
    }
}
