package app.fitness.controllers;

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

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "signIn";
        }
        System.out.println("Age = " +user.getAge());
        userService.addUser(user);
        model.addAttribute("user", user);
        System.out.println("User added");
        return "saveUser";
    }
}
