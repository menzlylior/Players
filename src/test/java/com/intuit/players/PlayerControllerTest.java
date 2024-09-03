package com.intuit.players;

import com.intuit.players.controller.PlayerController;
import com.intuit.players.model.Player;
import com.intuit.players.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

class PlayerControllerTest {

    @Mock
    private PlayerService<Player> playerService;

    @InjectMocks
    private PlayerController playerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPlayers_ReturnsListOfPlayers() {
        Player player1 = new Player();
        player1.setPlayerID("P1");
        Player player2 = new Player();
        player2.setPlayerID("P2");

        when(playerService.getAll()).thenReturn(Arrays.asList(player1, player2));

        ResponseEntity<List<Player>> response = playerController.getAllPlayers();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getPlayerById_ReturnsPlayer_WhenFound() {
        Player player = new Player();
        player.setPlayerID("P1");
        when(playerService.getById("P1")).thenReturn(player);

        ResponseEntity<Player> response = playerController.getPlayerById("P1");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("P1", response.getBody().getPlayerID());
    }

    @Test
    void getPlayerById_ReturnsNotFound_WhenNotFound() {
        when(playerService.getById("P1")).thenReturn(null);

        ResponseEntity<Player> response = playerController.getPlayerById("P1");

        assertEquals(NOT_FOUND.value(), response.getStatusCode().value());
    }
}
