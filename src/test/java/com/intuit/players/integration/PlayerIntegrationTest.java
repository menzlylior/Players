package com.intuit.players.integration;

import com.intuit.players.model.Player;
import com.intuit.players.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PlayerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @BeforeEach
    void setUp() {
        playerRepository.deleteAll();
        Player player = new Player("player1", 1980, 5, 12, "USA", "CA", "Los Angeles",
                null, null, null, null, null, null,
                "John", "Doe", "Johnathan Doe", 200.0, 75.0,
                null, null, LocalDate.of(2001, 4, 3), LocalDate.of(2010, 9, 28),
                "doej001", "jdoe");
        playerRepository.save(player);
    }

    @Test
    public void testGetAllPlayers() throws Exception {
        mockMvc.perform(get("/api/players")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].playerID").value("player1"));
    }

    @Test
    public void testGetPlayerById() throws Exception {
        mockMvc.perform(get("/api/players/player1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playerID").value("player1"))
                .andExpect(jsonPath("$.nameFirst").value("John"));
    }

    @Test
    public void testGetPlayerById_NotFound() throws Exception {
        mockMvc.perform(get("/api/players/invalidID")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
