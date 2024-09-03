package com.intuit.players.service;

import java.util.List;

public interface PlayerService<T> {

    /**
     * Saves a list of entities to the database.
     *
     * @param entities the list of entities to save
     */
    void saveAll(List<T> entities);

    /**
     * Retrieves all entities from the database.
     *
     * @return a list of all entities
     */
    List<T> getAll();

    /**
     * Retrieves an entity by its ID.
     *
     * @param id the entity's ID
     * @return the entity, or null if not found
     */
    T getById(String id);
}
