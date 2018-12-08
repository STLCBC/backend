package com.stlcbc.backend.controllers;

import com.stlcbc.backend.http.OktaClient;
import com.stlcbc.backend.models.User;
import com.stlcbc.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController("/users")
public class UsersController {

    private OktaClient oktaClient;

    private UserRepository userRepository;

    @Value("${auth.api-token}")
    private String oktaToken;

    @Autowired
    public UsersController(OktaClient oktaClient, UserRepository userRepository){
        this.oktaClient = oktaClient;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody User user){

        Optional<User> existingUser = userRepository.findByUsernameIs(user.getUsername());

        if(existingUser.isPresent()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Map<String, String> profile = new HashMap<>();
        profile.put("firstName", user.getFirstName());
        profile.put("lastName", user.getLastName());
        profile.put("email", user.getUsername());
        profile.put("login", user.getUsername());

        Map<String, String> credentials = new HashMap<>();
        credentials.put("password", user.getPassword());

        Map<String, Object> request = new HashMap<>();
        request.put("profile", profile);
        request.put("credentials", credentials);

        Map<String, Object> createdUser = oktaClient.createUser("SSWS " + oktaToken, request);
        user.setIsAdmin(false);
        user.setOktaId((String) createdUser.get("id"));

        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
