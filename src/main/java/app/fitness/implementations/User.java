package app.fitness.implementations;


import javax.persistence.*;
import java.util.Date;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    //private Date dateOfBirth;
    private int age;
    @OneToOne(cascade = CascadeType.PERSIST)
    private BodyParameters bodyParameters = new BodyParameters();
	
    public User() {}

    public User(Long id, String name, int age /*Date dateOfBirth*/) {
        super();
        this.id = id;
        this.name = name;
        //this.dateOfBirth = dateOfBirth;
        this.age = age;
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

   /* public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }*/

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BodyParameters getBodyParameters() {
        return bodyParameters;
    }

    public void setBodyParameters(BodyParameters bodyParameters) {
        this.bodyParameters = bodyParameters;
    }
}
