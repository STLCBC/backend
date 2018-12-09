package com.stlcbc.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@NamedEntityGraphs({
        @NamedEntityGraph(name = "Event.attendees", attributeNodes = {
                @NamedAttributeNode("attendees")
        }),
        @NamedEntityGraph(name = "Event.brewery", attributeNodes = {
                @NamedAttributeNode("brewery")
        }),
        @NamedEntityGraph(name = "Event.allJoins", attributeNodes = {
                @NamedAttributeNode("attendees"),
                @NamedAttributeNode("brewery")
        }),
        @NamedEntityGraph(name = "Event.breweryAndRatings", attributeNodes = {
                @NamedAttributeNode("brewery"),
                @NamedAttributeNode(value = "ratings")
        })
})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss a")
    private LocalDateTime at;

    private String location;

    private UUID code;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "event_attendees",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"))
    private Set<User> attendees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brewery_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Brewery brewery;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "event")
    private Set<Rating> ratings;
}
