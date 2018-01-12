package com.github.thushear.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.github.thushear.service.BlogService;
import com.github.thushear.web.rest.errors.BadRequestAlertException;
import com.github.thushear.web.rest.util.HeaderUtil;
import com.github.thushear.web.rest.util.PaginationUtil;
import com.github.thushear.service.dto.BlogDTO;
import com.github.thushear.service.dto.BlogCriteria;
import com.github.thushear.service.BlogQueryService;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Blog.
 */
@RestController
@RequestMapping("/api")
public class BlogResource {

    private final Logger log = LoggerFactory.getLogger(BlogResource.class);

    private static final String ENTITY_NAME = "blog";

    private final BlogService blogService;

    private final BlogQueryService blogQueryService;

    public BlogResource(BlogService blogService, BlogQueryService blogQueryService) {
        this.blogService = blogService;
        this.blogQueryService = blogQueryService;
    }

    /**
     * POST  /blogs : Create a new blog.
     *
     * @param blogDTO the blogDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new blogDTO, or with status 400 (Bad Request) if the blog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/blogs")
    @Timed
    public ResponseEntity<BlogDTO> createBlog(@RequestBody BlogDTO blogDTO) throws URISyntaxException {
        log.debug("REST request to save Blog : {}", blogDTO);
        if (blogDTO.getId() != null) {
            throw new BadRequestAlertException("A new blog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlogDTO result = blogService.save(blogDTO);
        return ResponseEntity.created(new URI("/api/blogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /blogs : Updates an existing blog.
     *
     * @param blogDTO the blogDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated blogDTO,
     * or with status 400 (Bad Request) if the blogDTO is not valid,
     * or with status 500 (Internal Server Error) if the blogDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/blogs")
    @Timed
    public ResponseEntity<BlogDTO> updateBlog(@RequestBody BlogDTO blogDTO) throws URISyntaxException {
        log.debug("REST request to update Blog : {}", blogDTO);
        if (blogDTO.getId() == null) {
            return createBlog(blogDTO);
        }
        BlogDTO result = blogService.save(blogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, blogDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /blogs : get all the blogs.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of blogs in body
     */
    @GetMapping("/blogs")
    @Timed
    public ResponseEntity<List<BlogDTO>> getAllBlogs(BlogCriteria criteria,@ApiParam Pageable pageable) {
        log.debug("REST request to get Blogs by criteria: {}", criteria);
        Page<BlogDTO> page = blogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blogs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /blogs/:id : get the "id" blog.
     *
     * @param id the id of the blogDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the blogDTO, or with status 404 (Not Found)
     */
    @GetMapping("/blogs/{id}")
    @Timed
    public ResponseEntity<BlogDTO> getBlog(@PathVariable Long id) {
        log.debug("REST request to get Blog : {}", id);
        BlogDTO blogDTO = blogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(blogDTO));
    }

    /**
     * DELETE  /blogs/:id : delete the "id" blog.
     *
     * @param id the id of the blogDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/blogs/{id}")
    @Timed
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        log.debug("REST request to delete Blog : {}", id);
        blogService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
