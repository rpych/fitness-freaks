package app.fitness.services;

import app.fitness.implementations.*;
import app.fitness.repositories.DailyExerciseRepository;
import app.fitness.repositories.LoggedExerciseRepository;
import app.fitness.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Qualifier("userService")
@Service
public class UserService {
    public List<DailyExercise> dailyExercises = new LinkedList<>();
    public List<LoggedExercise> loggedDailyExercises = new LinkedList<>();

    private UserRepository userRepository;
    private DailyExerciseRepository dailyExerciseRepository;
    private LoggedExerciseRepository loggedExerciseRepository;
    private ExercisesSolver exercisesSolver;

    @Autowired
    public UserService(UserRepository userRepository,DailyExerciseRepository dailyExerciseRepository,
                       LoggedExerciseRepository loggedExerciseRepository, ExercisesSolver exercisesSolver) {
        this.userRepository = userRepository;
        this.exercisesSolver = exercisesSolver;
        this.dailyExerciseRepository = dailyExerciseRepository;
        this.loggedExerciseRepository = loggedExerciseRepository;
    }

    public UserService() {
    }

    public void saveDailyExerciseByUserId(Long id, DailyExercise dailyExercise){
        dailyExercise.setId(id);
        dailyExerciseRepository.save(dailyExercise);
    }

    public List<DailyExercise> getDailyExercisesForUserId(Long id){
        Long ids[] = {id};
       return dailyExerciseRepository.getDailyExercisesByUserId(id);
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

    public Map<Integer, List<DailyExercise> > prepareExercisesForGivenPeriodOfTime(Integer numOfDays, Integer relShape){
       Map<Integer, List<DailyExercise> > exercisesForNumOfDays = new HashMap<>();
        for(int i=0;i<numOfDays;++i){
            List<DailyExercise> exersListForOneDay = prepareExercisesForOneDay(relShape);
            exercisesForNumOfDays.put(i, exersListForOneDay);
        }
        return exercisesForNumOfDays;
    }

    public List<DailyExercise> prepareExercisesForOneDay(Integer relShape){
        List<DailyExercise> exercisesForOneDay = new LinkedList<>();
        Integer exersNum = exercisesSolver.getExerciseNames().size();
        for(int i=0;i<exersNum;++i){
            String exerName = exercisesSolver.getExerciseNames().get(i);
            DailyExercise ex = exercisesSolver.getCustomExercise("Day"+i, exerName, relShape);
            exercisesForOneDay.add(ex);
        }
        return exercisesForOneDay;
    }

}
