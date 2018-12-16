package com.stlcbc.backend.controllers;

import com.stlcbc.backend.exceptions.ModelNotFound;
import com.stlcbc.backend.models.Brewery;
import com.stlcbc.backend.models.Event;
import com.stlcbc.backend.models.User;
import com.stlcbc.backend.repositories.BreweryRepository;
import com.stlcbc.backend.repositories.EventRepository;
import com.stlcbc.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private EventRepository eventRepository;
    private BreweryRepository breweryRepository;
    private UserRepository userRepository;

    @Autowired
    public EventController(EventRepository eventRepository, BreweryRepository breweryRepository, UserRepository userRepository){
        this.eventRepository = eventRepository;
        this.breweryRepository = breweryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    List<Event> index(){
        return this.eventRepository.findAllWithBreweryRatingsBy();
    }

    @GetMapping("/{id}")
    Event view(@PathVariable("id") Long eventId){
        Optional<Event> event = this.eventRepository.findWithBreweryRatingsById(eventId);

        if(!event.isPresent()){
            throw new ModelNotFound("event");
        }

        return event.get();
    }

    @PostMapping("/{breweryId}")
    ResponseEntity<?> create(@PathVariable("breweryId") Long breweryId, @RequestBody Event event, @AuthenticationPrincipal JwtAuthenticationToken principal){
        String uid = (String) principal.getTokenAttributes().get("uid");
        Optional<User> optionalUser = this.userRepository.findByOktaId(uid);

        if(!optionalUser.isPresent()){
            throw new ModelNotFound("user");
        }

        User user = optionalUser.get();
        if(!user.getIsAdmin()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Optional<Brewery> breweryOptional = this.breweryRepository.findWithEventsById(breweryId);

        if(!breweryOptional.isPresent()){
            throw new ModelNotFound("brewery");
        }

        Brewery brewery = breweryOptional.get();

        if(brewery.getEvents() == null){
            brewery.setEvents(new HashSet<>());
        }
        event.setCode(UUID.randomUUID());
        event.setBrewery(brewery);
        brewery.getEvents().add(event);
        this.breweryRepository.save(brewery);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/validate/{code}")
    ResponseEntity<?> validateCode(@PathVariable("code") String code){
        try{
            UUID eventCode = UUID.fromString(code);

            Optional<Event> event = this.eventRepository.findByCode(eventCode);

            return event.map(event1 -> new ResponseEntity<>(event1, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
