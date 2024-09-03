package com.intuit.players;

import com.intuit.players.model.Player;
import com.intuit.players.enums.BattingHand;
import com.intuit.players.enums.ThrowingHand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PlayerValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidPlayer() {
        Player player = new Player();
        player.setPlayerID("player1");
        player.setBirthYear(1980);
        player.setBirthMonth(5);
        player.setBirthDay(12);
        player.setBirthCountry("USA");
        player.setNameFirst("John");
        player.setNameLast("Doe");
        player.setWeight(200.0);
        player.setHeight(75.0);
        player.setBats(BattingHand.R);
        player.setThrowingHand(ThrowingHand.R);
        player.setDebut(LocalDate.of(2001, 4, 3));
        player.setFinalGame(LocalDate.of(2010, 9, 28));

        Set<ConstraintViolation<Player>> violations = validator.validate(player);

        assertTrue(violations.isEmpty(), "Expected no violations but got " + violations);
    }

    @Test
    void testInvalidPlayer_PastOrPresentViolation() {
        Player player = new Player();
        player.setPlayerID("player1");
        player.setBirthYear(1980);
        player.setBirthMonth(5);
        player.setBirthDay(12);
        player.setBirthCountry("USA");
        player.setNameFirst("John");
        player.setNameLast("Doe");
        player.setWeight(200.0);
        player.setHeight(75.0);
        player.setBats(BattingHand.R);
        player.setThrowingHand(ThrowingHand.R);
        player.setDebut(LocalDate.of(2001, 4, 3));
        player.setFinalGame(LocalDate.of(2030, 9, 28)); // Invalid date in the future

        Set<ConstraintViolation<Player>> violations = validator.validate(player);

        assertFalse(violations.isEmpty(), "Expected violations for future final game date");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("finalGame") && v.getMessage().contains("must be a date in the past or in the present")));
    }

    @Test
    void testInvalidPlayer_NotNullViolation() {
        Player player = new Player();
        player.setPlayerID(null); // This should cause a violation
        player.setBirthYear(1980);
        player.setBirthMonth(5);
        player.setBirthDay(12);
        player.setBirthCountry("USA");
        player.setNameFirst("John");
        player.setNameLast("Doe");
        player.setWeight(200.0);
        player.setHeight(75.0);
        player.setBats(BattingHand.R);
        player.setThrowingHand(ThrowingHand.R);
        player.setDebut(LocalDate.of(2001, 4, 3));
        player.setFinalGame(LocalDate.of(2010, 9, 28));

        Set<ConstraintViolation<Player>> violations = validator.validate(player);

        assertFalse(violations.isEmpty(), "Expected violations for null playerID");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("playerID") && v.getMessage().contains("must not be null")));
    }
}
