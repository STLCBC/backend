package com.stlcbc.backend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "breweries")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@NamedEntityGraphs({
        @NamedEntityGraph(name = "Brewery.ratings", attributeNodes = {
                @NamedAttributeNode("ratings")
        }),
        @NamedEntityGraph(name = "Brewery.events", attributeNodes = {
                @NamedAttributeNode("events")
        }),
        @NamedEntityGraph(name = "Brewery.allJoins", attributeNodes = {
                @NamedAttributeNode("ratings"),
                @NamedAttributeNode("events")
        })
})
public class Brewery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Boolean visited;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "brewery", fetch = FetchType.LAZY)
    private Set<Rating> ratings;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "brewery", fetch = FetchType.LAZY)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<Event> events;
}
