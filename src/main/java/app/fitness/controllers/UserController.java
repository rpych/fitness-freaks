package app.fitness.controllers;

import app.fitness.implementations.*;
import app.fitness.payload.ApiResponse;
import app.fitness.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import app.fitness.implementations.User;
import app.fitness.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String home(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/oauth2/redirect")
    public String redirect(Model model) {
        return "redirect";
    }

    @GetMapping("/oauth2/logInto/{userId}")
    public String getUserLogById(@PathVariable("userId") Long userId, Model model){
        Long id = new Long(-1);
        if(userRepository.findById(userId).isPresent())
            userId = userRepository.findById(userId).get().getId();
        System.out.println("User  "+ "," + "userId = " + userId);
        User user = new User();
        user.setId(userId); //!!!!!!!!!!!!
        model.addAttribute("user", user);
        DailyExercise dexer = new DailyExercise();
        model.addAttribute("dailyExer", dexer);
        return "logInto";
    }

    @PostMapping(value = "/oauth2/getCustomizedExercises/{userId}")
    public String getCustomizedExercises(@PathVariable("userId") Long id, @ModelAttribute User user, BindingResult result, Model model){
        if(result.hasErrors()){
            System.out.println("Error in binding in getCustomizedExercises");
            return "logInto";
        }
        user.setId(id);
        model.addAttribute("user", user);
        System.out.println("User in getCustomizedExercises = " +  user.getId());
        Integer defaultNumOfDaysForTraining = 7;
        Integer relShape = user.getBodyParameters().getRelativeShape();
        System.out.println("RelShape = " + relShape);
        Map<Integer, List<DailyExercise>> exercisesForNumOfDays = userService.prepareExercisesForGivenPeriodOfTime(defaultNumOfDaysForTraining,
                relShape, id);
        userService.saveDailyExercises(exercisesForNumOfDays);
        System.out.println("Size = " + exercisesForNumOfDays.size());
        for(Map.Entry<Integer, List<DailyExercise> > e: exercisesForNumOfDays.entrySet()){
            for(DailyExercise ex: e.getValue())
                System.out.println("Key = " + e.getKey() + ", Values = " + ex.getName() + ", id = "+ex.getId());
        }

        model.addAttribute("exercisesMap", exercisesForNumOfDays);
        return "getCustomizedExercises";
    }

    @RequestMapping(value = "/oauth2/arrangeTrainingPart/{id}", method = RequestMethod.POST)
    public String arrangeTrainingPart(@PathVariable("id") Long id, @RequestBody DailyExercise exer, BindingResult result, Model model){
        System.out.println("Exercise successfully posted with exer date = " + exer.getDate() + "  exer name = "+ exer.getName() + " exer rounds = "+exer.getRounds());
        userService.dailyExercises.add(exer);
        userService.saveDailyExerciseByUserId(id, exer);
        /*if(!userService.saveDailyExerciseByUserId(id, exer)){
            return "exerciseInUse";
        }*/
        return "arrangeTrainingPart";
    }

    @GetMapping("/oauth2/arrangeTraining/{userId}")
    public String arrangeTraining(@PathVariable("userId") Long userId, Model model){
        Long id = new Long(-1);
        if(userRepository.findById(userId).isPresent())
            userId = userRepository.findById(userId).get().getId();
        System.out.println("UserId in arrangeTraining = "+ userId);
        User user = new User();
        user.setId(userId); //!!!!!!!!!!!!
        model.addAttribute("user", user);
        //System.out.println("UserPrincipal = " + userPrincipal.getId() + ", " + userPrincipal.getName());
        List<DailyExercise> dailyExercises = userService.getDailyExercisesByUserId(userId, false); //isLogged = false
        model.addAttribute("exercisesList", dailyExercises);
        LoggedExercise logEx = new LoggedExercise();
        for(DailyExercise ex: dailyExercises){
            System.out.println(ex.getDate() + " ," + ex.getName());
        }
        model.addAttribute("loggedExercise", logEx);
        return "arrangeTraining";
    }

    @PostMapping("/oauth2/logDailyExercise/{userId}")
    public String logDailyExercise(@PathVariable("userId") Long userId, @ModelAttribute LoggedExercise logExer, BindingResult result, Model model){
        logExer.setId(userId);
        userService.loggedDailyExercises.add(logExer);
        if(!userService.saveLoggedExerciseByUserId(userId, logExer)){
            model.addAttribute("userId", logExer.getId());
            return "exerciseInUse";
        }
        userService.updateDailyExerciseIsLogged(logExer);
        model.addAttribute("logExer", logExer);
        System.out.println("Logged exercise with exer date = " + logExer.getDate() + "  exer name = "+ logExer.getName() + " exer rounds = "+logExer.getAllRepetitions());
        return "logDailyExercise";
    }

    @GetMapping("/oauth2/getUserExercises/{userId}")
    public String getUserExercises(@PathVariable("userId") Long userId, Model model){
        int limit = 5;
        List<ExerciseComparison> exercisesList = userService.getComparisonBetweenLoggedAndAssumedExercises(userId, limit);
        for(ExerciseComparison ex: exercisesList){
            System.out.println("Exercise in getUserExercise = " + ex.getName() + ", " + ex.getDate() + ", "+ ex.getAssumedRepetitions() + ", " + ex.getLoggedRepetitions());
        }
        model.addAttribute("exercisesList", exercisesList);
        model.addAttribute("userId", userId);
        return "userById";
    }

    @DeleteMapping("/oauth2/deleteElement/{userId}/{exerId}")
    public ResponseEntity<?> deleteUserExercise(@PathVariable("userId") Long userId, @PathVariable("exerId") Long exerId){
        System.out.println("In deleteElement Userid = " + userId + ", exerId = "+ exerId);
        userService.deleteUserExercise(exerId);
        return ResponseEntity.ok(new ApiResponse(true, "Exercise deleted."));
    }
}
