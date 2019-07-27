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
        DailyExercise res = dailyExerciseRepository.findDailyExerciseByIdAndNameAndDate(dailyExercise.getId(),
                dailyExercise.getName(), dailyExercise.getDate());
        if(res == null){
            dailyExerciseRepository.save(dailyExercise);
            return;
        }
        System.out.println("DailyExercise already in database");

    }

    public void saveLoggedExerciseByUserId(Long id, LoggedExercise loggedExercise){
        loggedExercise.setId(id);
        LoggedExercise res = loggedExerciseRepository.findLoggedExerciseByIdAndNameAndDate(loggedExercise.getId(),
                loggedExercise.getName(), loggedExercise.getDate());
        if(res == null){
            loggedExerciseRepository.save(loggedExercise);
            return;
        }
        System.out.println("LoggedExercise already in database");
    }

    public List<DailyExercise> getDailyExercisesByUserId(Long id){
       return dailyExerciseRepository.getDailyExercisesById(id);
    }

    public List<LoggedExercise> getLoggedExercisesByUserId(Long id){
        return loggedExerciseRepository.findAllById(id);
    }

    public LoggedExercise getLoggedExerciseCorrespondingDailyExercise(List<LoggedExercise> loggedExercises, DailyExercise dex){
        for(LoggedExercise logEx: loggedExercises){
            if(logEx.getDate().equals(dex.getDate()) && logEx.getName().equals(dex.getName())){
                return logEx;
            }
        }
        return null;
    }

    public List<ExerciseComparison> getComparisonBetweenLoggedAndAssumedExercises(Long userId, int limit){
        List<ExerciseComparison> exerciseComparisons = new LinkedList<>();
        List<LoggedExercise> loggedExercises = getLoggedExercisesByUserId(userId);
        List<DailyExercise>  assumedExercises = getDailyExercisesByUserId(userId);
        int counter = 0;
        for(DailyExercise dex: assumedExercises){
            if(counter++ > limit){ break; }
            LoggedExercise logEx = getLoggedExerciseCorrespondingDailyExercise(loggedExercises, dex);
            if(logEx != null){
                ExerciseComparison exComp = new ExerciseComparison(logEx.getName(), logEx.getDate(),
                        logEx.getAllRepetitions(), dex.getRounds() * dex.getRepetitionsInOneRound());
                exerciseComparisons.add(exComp);
            }
        }
        return exerciseComparisons;
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
