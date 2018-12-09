package com.stlcbc.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rating")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@NamedEntityGraphs({
        @NamedEntityGraph(name = "Rating.brewery", attributeNodes = {
                @NamedAttributeNode("brewery")
        }),
        @NamedEntityGraph(name = "Rating.user", attributeNodes = {
                @NamedAttributeNode("user")
        }),
        @NamedEntityGraph(name = "Rating.allJoins", attributeNodes = {
                @NamedAttributeNode("brewery"),
                @NamedAttributeNode("user")
        })
})
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double beer;

    private Double experience;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brewery_id")
    private Brewery brewery;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;
}
