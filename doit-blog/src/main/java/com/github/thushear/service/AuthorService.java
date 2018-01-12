package com.github.thushear.service;

import com.github.thushear.domain.Author;
import java.util.List;

/**
 * Service Interface for managing Author.
 */
public interface AuthorService {

    /**
     * Save a author.
     *
     * @param author the entity to save
     * @return the persisted entity
     */
    Author save(Author author);

    /**
     *  Get all the authors.
     *
     *  @return the list of entities
     */
    List<Author> findAll();

    /**
     *  Get the "id" author.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Author findOne(Long id);

    /**
     *  Delete the "id" author.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
