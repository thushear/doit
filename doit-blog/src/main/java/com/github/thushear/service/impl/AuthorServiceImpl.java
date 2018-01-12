package com.github.thushear.service.impl;

import com.github.thushear.service.AuthorService;
import com.github.thushear.domain.Author;
import com.github.thushear.repository.AuthorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Author.
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService{

    private final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * Save a author.
     *
     * @param author the entity to save
     * @return the persisted entity
     */
    @Override
    public Author save(Author author) {
        log.debug("Request to save Author : {}", author);
        return authorRepository.save(author);
    }

    /**
     *  Get all the authors.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Author> findAll() {
        log.debug("Request to get all Authors");
        return authorRepository.findAll();
    }

    /**
     *  Get one author by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Author findOne(Long id) {
        log.debug("Request to get Author : {}", id);
        return authorRepository.findOne(id);
    }

    /**
     *  Delete the  author by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Author : {}", id);
        authorRepository.delete(id);
    }
}
