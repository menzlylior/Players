package com.intuit.players.service;

import com.intuit.players.exception.CsvParsingException;
import com.intuit.players.model.Player;
import com.intuit.players.repository.PlayerRepository;
import com.intuit.players.helpers.PlayerCsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService<Player> {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerCsvParser playerCsvParser;

    @PostConstruct
    @Transactional
    public void init() {
        try {
            // Clear existing players
            playerRepository.deleteAll();

            // Load new players from the CSV file
            List<Player> players = playerCsvParser.parseCsvToPlayers("player.csv");
            playerRepository.saveAll(players);
        } catch (CsvParsingException e) {
            e.getErrorMessages().forEach(System.err::println);
            throw e;
        }
    }

    @Override
    public void saveAll(List<Player> entities) {
        playerRepository.saveAll(entities);
    }

    @Override
    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    @Override
    public Player getById(String id) {
        return playerRepository.findById(id).orElse(null);
    }
}
