package com.stlcbc.backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@NamedEntityGraphs({
        @NamedEntityGraph(name="User.events", attributeNodes = {
                @NamedAttributeNode("events")
        }),
        @NamedEntityGraph(name="User.ratings", attributeNodes = {
                @NamedAttributeNode("ratings")
        }),
        @NamedEntityGraph(name="User.allJoins", attributeNodes = {
                @NamedAttributeNode("events"),
                @NamedAttributeNode("ratings")
        })
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private String password;

    private String oktaId;

    private Boolean isAdmin;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "event_attendees",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<Event> events;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Rating> ratings;
}
