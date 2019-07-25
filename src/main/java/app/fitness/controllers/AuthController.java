package app.fitness.controllers;

import app.fitness.exceptions.BadRequestException;
import app.fitness.implementations.AuthProvider;
import app.fitness.implementations.User;
import app.fitness.payload.ApiResponse;
import app.fitness.payload.AuthResponse;
import app.fitness.payload.LoginRequest;
import app.fitness.payload.SignUpRequest;
import app.fitness.repositories.UserRepository;
import app.fitness.security.TokenProvider;
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

    @PostMapping("/oauth2/redirect/log")
    public /*ResponseEntity<?>*/ String authenticateUser(@Valid @RequestBody LoginRequest loginRequest, Model model) {

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
    public /*ResponseEntity<?>*/ String registerUser(@Valid @RequestBody SignUpRequest signUpRequest, Model model) {
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
    }

}
