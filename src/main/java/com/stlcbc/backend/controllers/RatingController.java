package com.stlcbc.backend.controllers;

import com.stlcbc.backend.exceptions.ModelNotFound;
import com.stlcbc.backend.models.Event;
import com.stlcbc.backend.models.Rating;
import com.stlcbc.backend.models.User;
import com.stlcbc.backend.models.transfer.EventCheckIn;
import com.stlcbc.backend.repositories.EventRepository;
import com.stlcbc.backend.repositories.RatingRepository;
import com.stlcbc.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private RatingRepository ratingRepository;

    @Autowired
    public RatingController(EventRepository eventRepository, UserRepository userRepository, RatingRepository ratingRepository){
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    @GetMapping("/{id}")
    public Rating view(@PathVariable("id") Long id){
        Optional<Rating> optionalRating = this.ratingRepository.findById(id);

        if(!optionalRating.isPresent()){
            throw new ModelNotFound("rating");
        }

        return optionalRating.get();
    }

    @PostMapping("/{eventId}")
    public ResponseEntity<?> create(@PathVariable("eventId") Long eventId, @Valid @RequestBody EventCheckIn eventCheckIn, @AuthenticationPrincipal JwtAuthenticationToken principal){
        String uid = (String) principal.getTokenAttributes().get("uid");
        Optional<User> optionalUser = this.userRepository.findByOktaId(uid);

        if(!optionalUser.isPresent()){
            throw new ModelNotFound("user");
        }

        User user = optionalUser.get();

        Optional<Event> optionalEvent = this.eventRepository.findWithBreweryRatingsById(eventId);

        if(!optionalEvent.isPresent()){
            throw new ModelNotFound("event");
        }

        Event event = optionalEvent.get();
        if(!event.getCode().equals(UUID.fromString(eventCheckIn.getCode()))){
            return new ResponseEntity<>("Invalid event code", HttpStatus.BAD_REQUEST);
        }


        Rating rating = eventCheckIn.getRating();


        rating.setUser(user);
        rating.setBrewery(event.getBrewery());
        rating.setEvent(event);

        if(event.getRatings() == null){
            event.setRatings(new HashSet<>());
        }

        event.getRatings().add(rating);

        this.eventRepository.save(event);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
