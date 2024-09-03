package com.intuit.players;

import com.intuit.players.model.Player;
import com.intuit.players.repository.PlayerRepository;
import com.intuit.players.helpers.PlayerCsvParser;
import com.intuit.players.service.PlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerCsvParser playerCsvParser;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void init_LoadsPlayersFromCsv() {
        Player player = createPlayer("P1");
        when(playerCsvParser.parseCsvToPlayers(anyString())).thenReturn(Arrays.asList(player));

        playerService.init();

        verify(playerRepository).deleteAll();
        verify(playerRepository).saveAll(anyList());
    }

    @Test
    void saveAll_SavesPlayers() {
        Player player = createPlayer("P1");

        playerService.saveAll(Arrays.asList(player));

        ArgumentCaptor<List<Player>> captor = ArgumentCaptor.forClass(List.class);
        verify(playerRepository).saveAll(captor.capture());

        List<Player> capturedPlayers = captor.getValue();
        assertEquals(1, capturedPlayers.size());
        assertEquals("P1", capturedPlayers.get(0).getPlayerID());
    }

    @Test
    void getAll_ReturnsAllPlayers() {
        Player player = createPlayer("P1");
        when(playerRepository.findAll()).thenReturn(Arrays.asList(player));

        List<Player> players = playerService.getAll();

        assertNotNull(players);
        assertEquals(1, players.size());
        assertEquals("P1", players.get(0).getPlayerID());
    }

    @Test
    void getById_ReturnsPlayer_WhenFound() {
        Player player = createPlayer("P1");
        when(playerRepository.findById("P1")).thenReturn(Optional.of(player));

        Player result = playerService.getById("P1");

        assertNotNull(result);
        assertEquals("P1", result.getPlayerID());
    }

    @Test
    void getById_ReturnsNull_WhenNotFound() {
        when(playerRepository.findById("P1")).thenReturn(Optional.empty());

        Player result = playerService.getById("P1");

        assertNull(result);
    }

    private Player createPlayer(String playerId) {
        Player player = new Player();
        player.setPlayerID(playerId);
        return player;
    }
}
