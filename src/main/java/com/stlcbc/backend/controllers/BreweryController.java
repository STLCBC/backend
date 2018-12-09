package com.stlcbc.backend.controllers;

import com.stlcbc.backend.exceptions.ModelNotFound;
import com.stlcbc.backend.models.Brewery;
import com.stlcbc.backend.models.User;
import com.stlcbc.backend.repositories.BreweryRepository;
import com.stlcbc.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController()
@RequestMapping("/api/breweries")
public class BreweryController {

    private BreweryRepository breweryRepository;
    private UserRepository userRepository;

    @Autowired
    public BreweryController(BreweryRepository breweryRepository, UserRepository userRepository){
        this.breweryRepository = breweryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    List<Brewery> index(){
        return this.breweryRepository.findAll();
    }

    @GetMapping("/{id}")
    Brewery view(@PathVariable("id") Long id){
        Optional<Brewery> brewery = this.breweryRepository.findById(id);

        if(!brewery.isPresent()){
            throw new ModelNotFound("brewery");
        }

        return brewery.get();
    }

    @PostMapping("/")
    ResponseEntity<?> create(@RequestBody Brewery brewery, @AuthenticationPrincipal JwtAuthenticationToken principal){
        String uid = (String) principal.getTokenAttributes().get("uid");
        Optional<User> optionalUser = this.userRepository.findByOktaId(uid);

        if(!optionalUser.isPresent()){
            throw new ModelNotFound("user");
        }

        User user = optionalUser.get();
        if(!user.getIsAdmin()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        this.breweryRepository.save(brewery);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
