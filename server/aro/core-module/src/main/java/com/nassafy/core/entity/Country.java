package com.nassafy.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Long countryId;
    private String country;
    private String countryEmoji;

    @OneToOne(mappedBy = "country", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    private MeteorInterest meteorInterest;

    public Country(Long countryId, String country, String countryEmoji) {
        this.countryId = countryId;
        this.country = country;
        this.countryEmoji = countryEmoji;
    }
}
