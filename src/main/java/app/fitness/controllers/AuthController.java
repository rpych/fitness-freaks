package app.fitness.controllers;

import app.fitness.exceptions.BadRequestException;
import app.fitness.implementations.*;
import app.fitness.payload.ApiResponse;
import app.fitness.payload.AuthResponse;
import app.fitness.payload.LoginRequest;
import app.fitness.payload.SignUpRequest;
import app.fitness.repositories.UserRepository;
import app.fitness.security.CurrentUser;
import app.fitness.security.TokenProvider;
import app.fitness.security.UserPrincipal;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Controller //Rest
//@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    /*@PostMapping("/oauth2/redirect/log")
    public ResponseEntity<?> String authenticateUser(@Valid @RequestBody LoginRequest loginRequest, Model model) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        User user = new User();
        model.addAttribute("user", user);
        System.out.println("User logged by me");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        //return ResponseEntity.ok(new AuthResponse(token));
        return "logInto";
    }

    @PostMapping("/oauth2/redirect/register")
    public ResponseEntity<?> String registerUser(@Valid @RequestBody SignUpRequest signUpRequest, Model model) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        System.out.println("User signupped by me");
        User user = new User();
        model.addAttribute("user", user);
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

       // return ResponseEntity.created(location)
         //       .body(new ApiResponse(true, "User registered successfully@"));
        return "logInto";
    }*/

    @PostMapping("/oauth2/authEmail")
    public String authForm(@ModelAttribute User user, Model model){
        Long userId = new Long(-1);
        if(userRepository.findByEmail(user.getEmail()).isPresent())
            userId = userRepository.findByEmail(user.getEmail()).get().getId();
        System.out.println("UserId in authForm = "+ userId);
        user.setId(userId); //!!!!!!!!!!!!
        model.addAttribute("user", user);
        model.addAttribute("banner", "Jesteś starym użytkownikiem i zostałeś teraz zalogowany");
        DailyExercise dexer = new DailyExercise();
        model.addAttribute("dailyExer", dexer);
        System.out.println("User added in authEmail");
        return "logInto";
    }


    @GetMapping("/oauth2/authForm")
    public String authForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "authForm";
    }

    /*@GetMapping("/oauth2/arrangeTraining")
    public ResponseEntity<?> String arrangeTraining(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String name = authentication.getName();
         System.out.println("Name = "+ name);
        /*if(userPrincipal==null){
            System.out.println("UserPrincipal is null ");
            return ResponseEntity.ok(new ApiResponse(false, "arrangeTraining is null"));
        }
        System.out.println("UserPrincipal id = " + userPrincipal.getId());
        LoggedExercise logEx = new LoggedExercise();
        String arrangeTraining = "arrangeTraining";
        //return ResponseEntity.ok(new ApiResponse(true, "arrangeTraining"));
    }*/




}
