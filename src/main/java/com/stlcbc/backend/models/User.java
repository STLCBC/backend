package com.stlcbc.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
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

    @Id @Getter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    private String username;

    @Getter
    @Column(name = "firstName")
    private String firstName;

    @Getter
    @Column(name = "lastName")
    private String lastName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private String password;

    @JsonIgnore
    public String getPassword(){
        return this.password;
    }

    @Getter
    @Column(name = "oktaId")
    private String oktaId;

    @Getter
    @Column(name = "isAdmin")
    private Boolean isAdmin;

    @Getter
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "event_attendees",
            joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<Event> events;

    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Rating> ratings;
}
