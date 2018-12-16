package com.stlcbc.backend.repositories;

import com.stlcbc.backend.models.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @EntityGraph(value = "Event.breweryAndRatings", type = EntityGraph.EntityGraphType.FETCH)
    List<Event> findAllWithBreweryRatingsBy();

    @EntityGraph(value = "Event.breweryAndRatings", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Event> findWithBreweryRatingsById(Long id);

    Optional<Event> findByCode(UUID code);
}
