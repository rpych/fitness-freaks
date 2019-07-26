package app.fitness.implementations;

import javax.persistence.*;

@Entity
public class BodyParameters {

    @Id
    private Long id;
    private Integer age;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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
