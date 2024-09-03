package com.intuit.players;

import com.intuit.players.helpers.PlayerCsvParser;
import com.intuit.players.model.Player;
import com.intuit.players.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerCsvParserTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerCsvParser playerCsvParser;

    private static String basePath;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        basePath = new File("").getAbsolutePath() + "/src/test/java/com/intuit/players/csvFiles/";
    }


    @Test
    void parseCsvToPlayers_ReturnsValidPlayers_WhenCsvIsValid() {
        String filePath = basePath + "valid-players.csv";

        when(playerRepository.existsById(anyString())).thenReturn(false);

        List<Player> players = playerCsvParser.parseCsvToPlayers(filePath);

        assertNotNull(players);
        assertEquals(3, players.size()); // Assuming the valid CSV has 3 valid entries

        verify(playerRepository, times(3)).existsById(anyString());
    }

    @Test
    void parseCsvToPlayers_LogsErrors_WhenCsvHasInvalidEntries() {
        String filePath = basePath + "invalid-players.csv";

        when(playerRepository.existsById(anyString())).thenReturn(false);

        List<Player> players = playerCsvParser.parseCsvToPlayers(filePath);

        assertNotNull(players);
        assertEquals(2, players.size()); // Assuming 2 valid entries and 1 invalid
    }

    @Test
    void parseCsvToPlayers_SkipsDuplicatePlayerIDs() {
        String filePath = basePath + "duplicate-players.csv";

        when(playerRepository.existsById("player1")).thenReturn(true);

        List<Player> players = playerCsvParser.parseCsvToPlayers(filePath);

        assertNotNull(players);
        assertTrue(players.size() == 1);

        verify(playerRepository, times(2)).existsById("player1");
    }

    @Test
    void parseCsvToPlayers_HandlesIOErrorGracefully() {
        String filePath = basePath + "non-existent-file.csv";

        List<Player> players = playerCsvParser.parseCsvToPlayers(filePath);

        assertNotNull(players);
        assertTrue(players.isEmpty());

        verifyNoInteractions(playerRepository);
    }
}
