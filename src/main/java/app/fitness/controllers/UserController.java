package app.fitness.controllers;

import app.fitness.implementations.*;
import app.fitness.payload.ApiResponse;
import app.fitness.payload.BodyParametersRequest;
import app.fitness.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(){
    }

    @GetMapping("/")
    public String home(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/user/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        return "userById";
    }

    @GetMapping("/user/remove/{id}")
    public String removeUser(@PathVariable("id") Long id, Model model){
        User user = userService.removeUser(id);
        model.addAttribute("user", user);
        return "userById";
    }

    @PostMapping("/logInto")
    public String saveUser(@ModelAttribute User user, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "home";
        }
        userService.addUser(user);
        model.addAttribute("user", user);
        Map<String, Exercise> attributes = new HashMap<>();
        attributes.put("exer1", new Exercise());
        attributes.put("exer2", new Exercise("Brzuszki"));
        attributes.put("exer3", new Exercise("Deska"));
        attributes.put("exer4", new Exercise("Podciąganie na drążku"));
        attributes.put("exer5", new Exercise("Martwy ciąg"));
        DailyExercise dexer = new DailyExercise();
        model.addAttribute("dailyExer", dexer);
        System.out.println("User added");
        return "logInto";
    }

    @PostMapping("/getCustomizedExercises")
    public String getCustomizedExercises(@ModelAttribute User user, BindingResult result, Model model){
        if(result.hasErrors()) {
            System.out.println("Errors in binding");
            return "logInto";
        }
        Integer defaultNumOfDaysForTraining = 7;
        Integer relShape = user.getBodyParameters().getRelativeShape();
        Map<Integer, List<Exercise>> exercisesForNumOfDays = userService.prepareExercisesForGivenPeriodOfTime(defaultNumOfDaysForTraining,
                relShape);
        System.out.println("Size = " + exercisesForNumOfDays.size());
        for(Map.Entry<Integer, List<Exercise> > e: exercisesForNumOfDays.entrySet()){
            for(Exercise ex: e.getValue())
                System.out.println("Key = " + e.getKey() + ", Values = " + ex.getName());
        }
        model.addAttribute("exercisesMap", exercisesForNumOfDays);
        return "getCustomizedExercises";
    }

    @RequestMapping(value = "/arrangeTrainingPart", method = RequestMethod.POST)
    public void arrangeTrainingPart(@RequestBody DailyExercise exer, BindingResult result, Model model){
        System.out.println("Exercise successfully posted with exer date = " + exer.getDate() + "  exer name = "+ exer.getName() + " exer rounds = "+exer.getRounds());
        userService.dailyExercises.add(exer);
    }

    @GetMapping("/arrangeTraining")
    public String arrangeTraining(Model model){
        model.addAttribute("exercisesList", userService.dailyExercises);
        LoggedExercise logEx = new LoggedExercise();
        for(DailyExercise ex: userService.dailyExercises){
            System.out.println(ex.getDate() + " ," + ex.getName());
        }
        model.addAttribute("loggedExercise", logEx);
        return "arrangeTraining";
    }



    @PostMapping("/logDailyExercise")
   //@RequestMapping(value = "/logDailyExercise", method = RequestMethod.POST ) // MediaType.APPLICATION_FORM_URLENCODED_VALUE
    public String logDailyExercise(@ModelAttribute LoggedExercise logExer, BindingResult result, Model model){
        userService.loggedDailyExercises.add(logExer);
        model.addAttribute("logExer", logExer);
        System.out.println("Logged exercise with exer date = " + logExer.getDate() + "  exer name = "+ logExer.getName() + " exer rounds = "+logExer.getAllRepetitions());
        return "logDailyExercise";
    }

    @GetMapping("/submitDailyExercises")
    public String submitDailyExercises(Model model){
        model.addAttribute("loggedExercises", userService.loggedDailyExercises);
        return "submitDailyExercises";
    }

}
*/

import app.fitness.exceptions.ResourceNotFoundException;
import app.fitness.implementations.User;
import app.fitness.repositories.UserRepository;
import app.fitness.security.CurrentUser;
import app.fitness.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@Controller //Rest
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


    @GetMapping("/oauth2/logInto")
    public String oauth2RedirectLog(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("banner", "Jesteś starym użytkownikiem i zostałeś teraz zalogowany");
        Map<String, Exercise> attributes = new HashMap<>();
        attributes.put("exer1", new Exercise());
        attributes.put("exer2", new Exercise("Brzuszki"));
        attributes.put("exer3", new Exercise("Deska"));
        attributes.put("exer4", new Exercise("Podciąganie na drążku"));
        attributes.put("exer5", new Exercise("Martwy ciąg"));
        DailyExercise dexer = new DailyExercise();
        model.addAttribute("dailyExer", dexer);
        System.out.println("User added in log");
        return "logInto";
    }

    @PostMapping(value = "/oauth2/getCustomizedExercises")
    //@PreAuthorize("hasRole('USER')")
    public /*ResponseEntity<?>*/ String getCustomizedExercises(@CurrentUser UserPrincipal userPrincipal, @RequestBody BodyParametersRequest parameters, Model model){
        System.out.println("User = " +  userPrincipal.getId() + ", " + userPrincipal.getName());
        Integer defaultNumOfDaysForTraining = 7;
        Integer relShape = parameters.getRelativeShape();//user.getBodyParameters().getRelativeShape();
        System.out.println("RelShape = " + relShape);
        Map<Integer, List<Exercise>> exercisesForNumOfDays = userService.prepareExercisesForGivenPeriodOfTime(defaultNumOfDaysForTraining,
                relShape);
        System.out.println("Size = " + exercisesForNumOfDays.size());
        for(Map.Entry<Integer, List<Exercise> > e: exercisesForNumOfDays.entrySet()){
            for(Exercise ex: e.getValue())
                System.out.println("Key = " + e.getKey() + ", Values = " + ex.getName());
        }
        /*URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(userPrincipal.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Exercises created."));*/
        //return ResponseEntity.ok(new ApiResponse(true, "Exercise updated."));
        model.addAttribute("exercisesMap", exercisesForNumOfDays);
        return "getCustomizedExercises";
    }

    @RequestMapping(value = "/oauth2/redirect/arrangeTrainingPart", method = RequestMethod.POST)
    public String arrangeTrainingPart(@RequestBody DailyExercise exer, BindingResult result, Model model){
        System.out.println("Exercise successfully posted with exer date = " + exer.getDate() + "  exer name = "+ exer.getName() + " exer rounds = "+exer.getRounds());
        userService.dailyExercises.add(exer);
        return "arrangeTrainingPart";
    }

    @GetMapping("/oauth2/redirect/arrangeTraining")
    public String arrangeTraining(Model model){
        model.addAttribute("exercisesList", userService.dailyExercises);
        LoggedExercise logEx = new LoggedExercise();
        for(DailyExercise ex: userService.dailyExercises){
            System.out.println(ex.getDate() + " ," + ex.getName());
        }
        model.addAttribute("loggedExercise", logEx);
        return "arrangeTraining";
    }



    @PostMapping("/oauth2/redirect/logDailyExercise")
    //@RequestMapping(value = "/logDailyExercise", method = RequestMethod.POST ) // MediaType.APPLICATION_FORM_URLENCODED_VALUE
    public String logDailyExercise(@ModelAttribute LoggedExercise logExer, BindingResult result, Model model){
        userService.loggedDailyExercises.add(logExer);
        model.addAttribute("logExer", logExer);
        System.out.println("Logged exercise with exer date = " + logExer.getDate() + "  exer name = "+ logExer.getName() + " exer rounds = "+logExer.getAllRepetitions());
        return "logDailyExercise";
    }

    @GetMapping("/oauth2/redirect/submitDailyExercises")
    public String submitDailyExercises(Model model){
        model.addAttribute("loggedExercises", userService.loggedDailyExercises);
        return "submitDailyExercises";
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

}
