package com.stlcbc.backend.repositories;

import com.stlcbc.backend.models.Brewery;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

public interface BreweryRepository extends JpaRepository<Brewery, Long> {

    @EntityGraph(value = "Brewery.events", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Brewery> findWithEventsById(Long id);
}
