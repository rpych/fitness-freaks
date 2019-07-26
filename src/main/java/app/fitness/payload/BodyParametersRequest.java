package app.fitness.payload;

import javax.validation.constraints.NotNull;

public class BodyParametersRequest {

    @NotNull
    private Integer age;
    @NotNull
    private Double weight;
    @NotNull
    private Double height;
    @NotNull
    private Integer relativeShape;


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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
