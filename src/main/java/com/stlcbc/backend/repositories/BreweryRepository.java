package com.stlcbc.backend.repositories;

import com.stlcbc.backend.models.Brewery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

public interface BreweryRepository extends JpaRepository<Brewery, Long> {

}
