package app.fitness.services;

import app.fitness.implementations.*;
import app.fitness.repositories.DailyExerciseRepository;
import app.fitness.repositories.LoggedExerciseRepository;
import app.fitness.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

    public void updateDailyExerciseIsLogged(LoggedExercise logEx){
        DailyExercise dex = dailyExerciseRepository.findDailyExerciseByIdAndNameAndDate(logEx.getId(), logEx.getName(), logEx.getDate());
        dex.setLogged(true);
        dailyExerciseRepository.save(dex);
    }

    public void saveDailyExercises(Map<Integer, List<DailyExercise>> dailyExercises){
        for(Map.Entry<Integer, List<DailyExercise> > e: dailyExercises.entrySet()){
            dailyExerciseRepository.saveAll(e.getValue());
        }

    }

    public List<DailyExercise> getDailyExercisesByUserId(Long id, boolean isLogged){
       return dailyExerciseRepository.getDailyExercisesByIdAndLogged(id, isLogged);
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
        List<DailyExercise>  assumedExercises = getDailyExercisesByUserId(userId, true);
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

    public Map<Integer, List<DailyExercise> > prepareExercisesForGivenPeriodOfTime(Integer numOfDays, Integer relShape, Long id){
       Map<Integer, List<DailyExercise> > exercisesForNumOfDays = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        for(int i=0;i<numOfDays;++i){
            calendar.add(Calendar.DAY_OF_YEAR, i);
            String date = new SimpleDateFormat("MM-dd-yyyy").format(calendar.getTime());
            System.out.println("Date = "+date);
            List<DailyExercise> exersListForOneDay = prepareExercisesForOneDay(relShape, id, date);
            exercisesForNumOfDays.put(i, exersListForOneDay);
        }
        return exercisesForNumOfDays;
    }

    public List<DailyExercise> prepareExercisesForOneDay(Integer relShape, Long id, String date){
        List<DailyExercise> exercisesForOneDay = new LinkedList<>();
        Integer exersNum = exercisesSolver.getExerciseNames().size();
        for(int i=0;i<exersNum;++i){
            String exerName = exercisesSolver.getExerciseNames().get(i);
            DailyExercise ex = exercisesSolver.getCustomExercise(id, date, exerName, relShape);
            exercisesForOneDay.add(ex);
        }
        return exercisesForOneDay;
    }

}
