package com.intuit.players.helpers;

import com.intuit.players.enums.BattingHand;
import com.intuit.players.enums.ThrowingHand;
import com.intuit.players.model.Player;
import com.intuit.players.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PlayerCsvParser {

    @Autowired
    private PlayerRepository playerRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Logger logger = LoggerFactory.getLogger(PlayerCsvParser.class);

    private boolean lineHasErrors = false;

    /**
     * Parses the CSV file and returns a list of Player entities. Collects errors during parsing
     * and logs the errors while returning only valid entries.
     *
     * @param filePath the path to the CSV file
     * @return a list of Player entities
     */
    public List<Player> parseCsvToPlayers(String filePath) {
        List<Player> players = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Set<String> existingPlayerIDs = new HashSet<>();
        String line;
        String splitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String header = br.readLine(); // Skip header, but keep columns count for validation
            int columnsCount = header.split(splitBy).length;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                String[] playerData = line.split(splitBy);
                lineHasErrors = false;

                if (playerData.length < columnsCount) {
                    errors.add("Line " + lineNumber + " has fewer columns than expected.");
                    continue;
                }
                String playerID = playerData[0];
                if (existingPlayerIDs.contains(playerID) || playerRepository.existsById(playerID)) {
                    errors.add("Duplicate playerID '" + playerID + "' found on line " + lineNumber);
                    continue;
                }
                existingPlayerIDs.add(playerID);

                try {
                    Player player = new Player();
                    player.setPlayerID(playerID);
                    player.setBirthYear(parseInteger(playerData[1], "birthYear", lineNumber, errors));
                    player.setBirthMonth(parseInteger(playerData[2], "birthMonth", lineNumber, errors));
                    player.setBirthDay(parseInteger(playerData[3], "birthDay", lineNumber, errors));
                    player.setBirthCountry(playerData[4]);
                    player.setBirthState(playerData[5]);
                    player.setBirthCity(playerData[6]);
                    player.setDeathYear(parseInteger(playerData[7], "deathYear", lineNumber, errors));
                    player.setDeathMonth(parseInteger(playerData[8], "deathMonth", lineNumber, errors));
                    player.setDeathDay(parseInteger(playerData[9], "deathDay", lineNumber, errors));
                    player.setDeathCountry(playerData[10]);
                    player.setDeathState(playerData[11]);
                    player.setDeathCity(playerData[12]);
                    player.setNameFirst(playerData[13]);
                    player.setNameLast(playerData[14]);
                    player.setNameGiven(playerData[15]);
                    player.setWeight(parseDouble(playerData[16], "weight", lineNumber, errors));
                    player.setHeight(parseDouble(playerData[17], "height", lineNumber, errors));
                    player.setBats(parseEnum(playerData[18], BattingHand.class, "bats", lineNumber, errors));
                    player.setThrowingHand(parseEnum(playerData[19], ThrowingHand.class, "throwingHand", lineNumber, errors));
                    player.setDebut(parseDate(playerData[20], "debut", lineNumber, errors));
                    player.setFinalGame(parseDate(playerData[21], "finalGame", lineNumber, errors));
                    player.setRetroID(playerData[22]);
                    player.setBbrefID(playerData[23]);

                    if (!lineHasErrors) {
                        players.add(player);
                    }
                } catch (Exception e) {
                    errors.add("Error parsing line " + lineNumber + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            errors.add("IO error reading the CSV file: " + e.getMessage());
        }

        int successCount = players.size();
        int errorCount = errors.size();
        logger.info("CSV Parsing complete: {} lines successfully parsed, {} lines failed.", successCount, errorCount);
        if (errorCount > 0) {
            logger.error("Errors encountered during parsing:\n{}", String.join("\n", errors));
        }

        return players;
    }

    private Integer parseInteger(String value, String fieldName, int lineNumber, List<String> errors) {
        try {
            return value != null && !value.isEmpty() ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            errors.add("Invalid integer for field '" + fieldName + "' on line " + lineNumber + ": " + value);
            lineHasErrors = true;
            return null;
        }
    }

    private Double parseDouble(String value, String fieldName, int lineNumber, List<String> errors) {
        try {
            return value != null && !value.isEmpty() ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            errors.add("Invalid double for field '" + fieldName + "' on line " + lineNumber + ": " + value);
            lineHasErrors = true;
            return null;
        }
    }

    private LocalDate parseDate(String value, String fieldName, int lineNumber, List<String> errors) {
        try {
            return value != null && !value.isEmpty() ? LocalDate.parse(value, DATE_FORMATTER) : null;
        } catch (DateTimeParseException e) {
            errors.add("Invalid date for field '" + fieldName + "' on line " + lineNumber + ": " + value);
            lineHasErrors = true;
            return null;
        }
    }

    private <E extends Enum<E>> E parseEnum(String value, Class<E> enumClass, String fieldName, int lineNumber, List<String> errors) {
        try {
            return value != null && !value.isEmpty() ? Enum.valueOf(enumClass, value) : null;
        } catch (IllegalArgumentException e) {
            errors.add("Invalid value for enum '" + fieldName + "' on line " + lineNumber + ": " + value);
            lineHasErrors = true;
            return null;
        }
    }
}
