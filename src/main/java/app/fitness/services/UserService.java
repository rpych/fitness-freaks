package app.fitness.services;

import app.fitness.implementations.Exercise;
import app.fitness.implementations.ExercisesSolver;
import app.fitness.implementations.User;
import app.fitness.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Qualifier("userService")
@Service
public class UserService {
    //private List<User> users = new LinkedList<>();

    private UserRepository userRepository;
    private ExercisesSolver exercisesSolver;

    @Autowired
    public UserService(UserRepository userRepository, ExercisesSolver exercisesSolver) {
        this.userRepository = userRepository;
        this.exercisesSolver = exercisesSolver;
    }

    public UserService() {
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User removeUser(Long id) {
       User user = getUser(id);
       userRepository.delete(user);
       return user;
    }

    public User getUser(Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
            return user.get();
        else
           return user.orElseThrow(() -> new IllegalArgumentException("User with id = " + id + " does not exist"));
    }

    public Map<Integer, List<Exercise> > prepareExercisesForGivenPeriodOfTime(Integer numOfDays, Integer relShape){
       Map<Integer, List<Exercise> > exercisesForNumOfDays = new HashMap<>();
        for(int i=0;i<numOfDays;++i){
            List<Exercise> exersListForOneDay = prepareExercisesForOneDay(relShape);
            exercisesForNumOfDays.put(i, exersListForOneDay);
        }
        return exercisesForNumOfDays;
    }

    public List<Exercise> prepareExercisesForOneDay(Integer relShape){
        List<Exercise> exercisesForOneDay = new LinkedList<>();
        Integer exersNum = exercisesSolver.getExerciseNames().size();
        for(int i=0;i<exersNum;++i){
            String exerName = exercisesSolver.getExerciseNames().get(i);
            Exercise ex = exercisesSolver.getCustomExercise(exerName, relShape);
            exercisesForOneDay.add(ex);
        }
        return exercisesForOneDay;
    }

}
