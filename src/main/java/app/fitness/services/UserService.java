package app.fitness.services;

import app.fitness.implementations.User;
import app.fitness.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    //private List<User> users = new LinkedList<>();

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserService() {
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
}
