package com.stlcbc.backend.models;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brewery_id")
    private Brewery brewery;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
