package com.github.thushear.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.github.thushear.domain.Blog;
import com.github.thushear.domain.*; // for static metamodels
import com.github.thushear.repository.BlogRepository;
import com.github.thushear.service.dto.BlogCriteria;

import com.github.thushear.service.dto.BlogDTO;
import com.github.thushear.service.mapper.BlogMapper;

/**
 * Service for executing complex queries for Blog entities in the database.
 * The main input is a {@link BlogCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link BlogDTO} or a {@link Page} of {%link BlogDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class BlogQueryService extends QueryService<Blog> {

    private final Logger log = LoggerFactory.getLogger(BlogQueryService.class);


    private final BlogRepository blogRepository;

    private final BlogMapper blogMapper;

    public BlogQueryService(BlogRepository blogRepository, BlogMapper blogMapper) {
        this.blogRepository = blogRepository;
        this.blogMapper = blogMapper;
    }

    /**
     * Return a {@link List} of {%link BlogDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BlogDTO> findByCriteria(BlogCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Blog> specification = createSpecification(criteria);
        return blogMapper.toDto(blogRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link BlogDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BlogDTO> findByCriteria(BlogCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Blog> specification = createSpecification(criteria);
        final Page<Blog> result = blogRepository.findAll(specification, page);
        return result.map(blogMapper::toDto);
    }

    /**
     * Function to convert BlogCriteria to a {@link Specifications}
     */
    private Specifications<Blog> createSpecification(BlogCriteria criteria) {
        Specifications<Blog> specification = Specifications.where(null);
//        if (criteria != null) {
//            if (criteria.getId() != null) {
//                specification = specification.and(buildSpecification(criteria.getId(), Blog_.id));
//            }
//            if (criteria.getTitle() != null) {
//                specification = specification.and(buildStringSpecification(criteria.getTitle(), Blog_.title));
//            }
//            if (criteria.getDesc() != null) {
//                specification = specification.and(buildStringSpecification(criteria.getDesc(), Blog_.desc));
//            }
//            if (criteria.getPublicDate() != null) {
//                specification = specification.and(buildRangeSpecification(criteria.getPublicDate(), Blog_.publicDate));
//            }
//        }
        return specification;
    }

}
