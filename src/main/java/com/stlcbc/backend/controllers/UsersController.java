package com.stlcbc.backend.controllers;

import com.stlcbc.backend.http.OktaClient;
import com.stlcbc.backend.models.User;
import com.stlcbc.backend.models.okta.OktaCredentials;
import com.stlcbc.backend.models.okta.OktaProfile;
import com.stlcbc.backend.models.okta.OktaUser;
import com.stlcbc.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private OktaClient oktaClient;

    private UserRepository userRepository;

    @Value("${auth.api-token}")
    private String oktaToken;

    @Value("${auth.group-id}")
    private String oktaGroupId;

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

        OktaProfile profile = new OktaProfile(user.getFirstName(), user.getLastName(), user.getUsername(), user.getUsername());
        OktaCredentials credentials = new OktaCredentials(user.getPassword());
        OktaUser oktaUser = new OktaUser(profile, credentials);

        Map<String, Object> createdUser = oktaClient.createUser("SSWS " + oktaToken, oktaUser);
        oktaClient.addUserToGroup("SSWS " + oktaToken, oktaGroupId, (String) createdUser.get("id"));
        user.setIsAdmin(false);
        user.setOktaId((String) createdUser.get("id"));

        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
