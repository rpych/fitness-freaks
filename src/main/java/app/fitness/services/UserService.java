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

    public boolean saveDailyExerciseByUserId(Long id, DailyExercise dailyExercise){
        dailyExercise.setId(id);
        List<DailyExercise> res = dailyExerciseRepository.findDailyExerciseByIdAndNameAndDate(dailyExercise.getId(),
                dailyExercise.getName(), dailyExercise.getDate());
        if(res.size() == 0){
            dailyExerciseRepository.save(dailyExercise);
            return true;
        }
        System.out.println("DailyExercise already in database");
        return false;
    }

    public boolean saveLoggedExerciseByUserId(Long id, LoggedExercise loggedExercise){
        loggedExercise.setId(id);
        LoggedExercise res = loggedExerciseRepository.findLoggedExerciseByIdAndNameAndDate(loggedExercise.getId(),
                loggedExercise.getName(), loggedExercise.getDate());
        if(res == null){
            loggedExerciseRepository.save(loggedExercise);
            return true;
        }
        System.out.println("LoggedExercise already in database");
        return false;
    }

    public void updateDailyExerciseIsLogged(LoggedExercise logEx){
        List<DailyExercise> dexList = dailyExerciseRepository.findDailyExerciseByIdAndNameAndDate(logEx.getId(), logEx.getName(), logEx.getDate());
        DailyExercise dex = dexList.get(0);
        dex.setLogged(true);
        dailyExerciseRepository.save(dex);
        if(dexList.size() > 1){
            for(int i=1;i<dexList.size();i++){
                DailyExercise d = dexList.get(i);
                dailyExerciseRepository.delete(d);
            }
        }
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

    public DailyExercise deleteUserExercise(Long exerciseId){
        DailyExercise dex = dailyExerciseRepository.findByPrimaryId(exerciseId);
        dailyExerciseRepository.delete(dex);
        return  dex;
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
        Date today = calendar.getTime();
        for(int i=0;i<numOfDays;++i){
            calendar.add(Calendar.DAY_OF_YEAR, 1);
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
