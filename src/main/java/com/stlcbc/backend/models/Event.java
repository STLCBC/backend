package com.stlcbc.backend.models;

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
        })
})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
    private Brewery brewery;
}
