package com.intuit.players.model;

import com.intuit.players.enums.BattingHand;
import com.intuit.players.enums.ThrowingHand;
import com.intuit.players.validation.ValidEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

/**
 * The Player class is designed to store the data parsed from a CSV file and
 * persists this data into a PostgreSQL database.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @NotNull
    @Size(max = 10)
    @Column(unique = true, nullable = false)
    private String playerID;
    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDay;
    private String birthCountry;
    private String birthState;
    private String birthCity;
    private Integer deathYear;
    private Integer deathMonth;
    private Integer deathDay;
    private String deathCountry;
    private String deathState;
    private String deathCity;

    @Size(max = 50)
    private String nameFirst;

    @Size(max = 50)
    private String nameLast;
    private String nameGiven;

    @Positive
    private Double weight;

    @Positive
    private Double height;

    @ValidEnum(enumClass = BattingHand.class)
    private BattingHand bats;

    @ValidEnum(enumClass = ThrowingHand.class)
    private ThrowingHand throwingHand;

    @PastOrPresent
    private LocalDate debut;

    @PastOrPresent
    private LocalDate finalGame;

    private String retroID;
    private String bbrefID;
}
