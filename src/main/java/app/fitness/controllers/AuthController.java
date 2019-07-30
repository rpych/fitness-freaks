package app.fitness.controllers;

import app.fitness.implementations.*;
import app.fitness.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {


    @Autowired
    private UserRepository userRepository;


    @PostMapping("/oauth2/authEmail")
    public String authForm(@ModelAttribute User user, Model model){
        Long userId = new Long(-1);
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            userId = userRepository.findByEmail(user.getEmail()).get().getId();
        if(userId == -1){
            return "error";
        }
        user.setId(userId);
        model.addAttribute("user", user);
        DailyExercise dexer = new DailyExercise();
        model.addAttribute("dailyExer", dexer);
        return "logInto";
    }


    @GetMapping("/oauth2/authForm")
    public String authForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "authForm";
    }
}
