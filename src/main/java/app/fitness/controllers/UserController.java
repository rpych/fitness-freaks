package app.fitness.controllers;

import app.fitness.implementations.Exercise;
import app.fitness.implementations.ExercisesSolver;
import app.fitness.implementations.User;
import app.fitness.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        attributes.put("exer1", new Exercise("Pompki"));
        attributes.put("exer2", new Exercise("Brzuszki"));
        attributes.put("exer3", new Exercise("Deska"));
        attributes.put("exer4", new Exercise("Podciąganie na drążku"));
        attributes.put("exer5", new Exercise("Martwy ciąg"));
        model.addAllAttributes(attributes);
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

}
