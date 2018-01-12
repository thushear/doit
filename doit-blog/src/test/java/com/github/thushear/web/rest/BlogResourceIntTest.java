package com.github.thushear.web.rest;

import com.github.thushear.ScaffoldApp;

import com.github.thushear.domain.Blog;
import com.github.thushear.repository.BlogRepository;
import com.github.thushear.service.BlogService;
import com.github.thushear.service.dto.BlogDTO;
import com.github.thushear.service.mapper.BlogMapper;
import com.github.thushear.web.rest.errors.ExceptionTranslator;
import com.github.thushear.service.dto.BlogCriteria;
import com.github.thushear.service.BlogQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.github.thushear.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BlogResource REST controller.
 *
 * @see BlogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScaffoldApp.class)
public class BlogResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PUBLIC_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLIC_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogQueryService blogQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBlogMockMvc;

    private Blog blog;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BlogResource blogResource = new BlogResource(blogService, blogQueryService);
        this.restBlogMockMvc = MockMvcBuilders.standaloneSetup(blogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Blog createEntity(EntityManager em) {
        Blog blog = new Blog()
            .title(DEFAULT_TITLE)
            .desc(DEFAULT_DESC)
            .publicDate(DEFAULT_PUBLIC_DATE)
            .content(DEFAULT_CONTENT);
        return blog;
    }

    @Before
    public void initTest() {
        blog = createEntity(em);
    }

    @Test
    @Transactional
    public void createBlog() throws Exception {
        int databaseSizeBeforeCreate = blogRepository.findAll().size();

        // Create the Blog
        BlogDTO blogDTO = blogMapper.toDto(blog);
        restBlogMockMvc.perform(post("/api/blogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogDTO)))
            .andExpect(status().isCreated());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeCreate + 1);
        Blog testBlog = blogList.get(blogList.size() - 1);
        assertThat(testBlog.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testBlog.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testBlog.getPublicDate()).isEqualTo(DEFAULT_PUBLIC_DATE);
        assertThat(testBlog.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createBlogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = blogRepository.findAll().size();

        // Create the Blog with an existing ID
        blog.setId(1L);
        BlogDTO blogDTO = blogMapper.toDto(blog);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBlogMockMvc.perform(post("/api/blogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBlogs() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList
        restBlogMockMvc.perform(get("/api/blogs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blog.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].publicDate").value(hasItem(DEFAULT_PUBLIC_DATE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getBlog() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get the blog
        restBlogMockMvc.perform(get("/api/blogs/{id}", blog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(blog.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.publicDate").value(DEFAULT_PUBLIC_DATE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getAllBlogsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where title equals to DEFAULT_TITLE
        defaultBlogShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the blogList where title equals to UPDATED_TITLE
        defaultBlogShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBlogsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultBlogShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the blogList where title equals to UPDATED_TITLE
        defaultBlogShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllBlogsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where title is not null
        defaultBlogShouldBeFound("title.specified=true");

        // Get all the blogList where title is null
        defaultBlogShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllBlogsByDescIsEqualToSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where desc equals to DEFAULT_DESC
        defaultBlogShouldBeFound("desc.equals=" + DEFAULT_DESC);

        // Get all the blogList where desc equals to UPDATED_DESC
        defaultBlogShouldNotBeFound("desc.equals=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    public void getAllBlogsByDescIsInShouldWork() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where desc in DEFAULT_DESC or UPDATED_DESC
        defaultBlogShouldBeFound("desc.in=" + DEFAULT_DESC + "," + UPDATED_DESC);

        // Get all the blogList where desc equals to UPDATED_DESC
        defaultBlogShouldNotBeFound("desc.in=" + UPDATED_DESC);
    }

    @Test
    @Transactional
    public void getAllBlogsByDescIsNullOrNotNull() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where desc is not null
        defaultBlogShouldBeFound("desc.specified=true");

        // Get all the blogList where desc is null
        defaultBlogShouldNotBeFound("desc.specified=false");
    }

    @Test
    @Transactional
    public void getAllBlogsByPublicDateIsEqualToSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where publicDate equals to DEFAULT_PUBLIC_DATE
        defaultBlogShouldBeFound("publicDate.equals=" + DEFAULT_PUBLIC_DATE);

        // Get all the blogList where publicDate equals to UPDATED_PUBLIC_DATE
        defaultBlogShouldNotBeFound("publicDate.equals=" + UPDATED_PUBLIC_DATE);
    }

    @Test
    @Transactional
    public void getAllBlogsByPublicDateIsInShouldWork() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where publicDate in DEFAULT_PUBLIC_DATE or UPDATED_PUBLIC_DATE
        defaultBlogShouldBeFound("publicDate.in=" + DEFAULT_PUBLIC_DATE + "," + UPDATED_PUBLIC_DATE);

        // Get all the blogList where publicDate equals to UPDATED_PUBLIC_DATE
        defaultBlogShouldNotBeFound("publicDate.in=" + UPDATED_PUBLIC_DATE);
    }

    @Test
    @Transactional
    public void getAllBlogsByPublicDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where publicDate is not null
        defaultBlogShouldBeFound("publicDate.specified=true");

        // Get all the blogList where publicDate is null
        defaultBlogShouldNotBeFound("publicDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBlogsByPublicDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where publicDate greater than or equals to DEFAULT_PUBLIC_DATE
        defaultBlogShouldBeFound("publicDate.greaterOrEqualThan=" + DEFAULT_PUBLIC_DATE);

        // Get all the blogList where publicDate greater than or equals to UPDATED_PUBLIC_DATE
        defaultBlogShouldNotBeFound("publicDate.greaterOrEqualThan=" + UPDATED_PUBLIC_DATE);
    }

    @Test
    @Transactional
    public void getAllBlogsByPublicDateIsLessThanSomething() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);

        // Get all the blogList where publicDate less than or equals to DEFAULT_PUBLIC_DATE
        defaultBlogShouldNotBeFound("publicDate.lessThan=" + DEFAULT_PUBLIC_DATE);

        // Get all the blogList where publicDate less than or equals to UPDATED_PUBLIC_DATE
        defaultBlogShouldBeFound("publicDate.lessThan=" + UPDATED_PUBLIC_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBlogShouldBeFound(String filter) throws Exception {
        restBlogMockMvc.perform(get("/api/blogs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(blog.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].publicDate").value(hasItem(DEFAULT_PUBLIC_DATE.toString())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBlogShouldNotBeFound(String filter) throws Exception {
        restBlogMockMvc.perform(get("/api/blogs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBlog() throws Exception {
        // Get the blog
        restBlogMockMvc.perform(get("/api/blogs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBlog() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);
        int databaseSizeBeforeUpdate = blogRepository.findAll().size();

        // Update the blog
        Blog updatedBlog = blogRepository.findOne(blog.getId());
        updatedBlog
            .title(UPDATED_TITLE)
            .desc(UPDATED_DESC)
            .publicDate(UPDATED_PUBLIC_DATE)
            .content(UPDATED_CONTENT);
        BlogDTO blogDTO = blogMapper.toDto(updatedBlog);

        restBlogMockMvc.perform(put("/api/blogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogDTO)))
            .andExpect(status().isOk());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeUpdate);
        Blog testBlog = blogList.get(blogList.size() - 1);
        assertThat(testBlog.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testBlog.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testBlog.getPublicDate()).isEqualTo(UPDATED_PUBLIC_DATE);
        assertThat(testBlog.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void updateNonExistingBlog() throws Exception {
        int databaseSizeBeforeUpdate = blogRepository.findAll().size();

        // Create the Blog
        BlogDTO blogDTO = blogMapper.toDto(blog);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBlogMockMvc.perform(put("/api/blogs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(blogDTO)))
            .andExpect(status().isCreated());

        // Validate the Blog in the database
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBlog() throws Exception {
        // Initialize the database
        blogRepository.saveAndFlush(blog);
        int databaseSizeBeforeDelete = blogRepository.findAll().size();

        // Get the blog
        restBlogMockMvc.perform(delete("/api/blogs/{id}", blog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Blog> blogList = blogRepository.findAll();
        assertThat(blogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Blog.class);
        Blog blog1 = new Blog();
        blog1.setId(1L);
        Blog blog2 = new Blog();
        blog2.setId(blog1.getId());
        assertThat(blog1).isEqualTo(blog2);
        blog2.setId(2L);
        assertThat(blog1).isNotEqualTo(blog2);
        blog1.setId(null);
        assertThat(blog1).isNotEqualTo(blog2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BlogDTO.class);
        BlogDTO blogDTO1 = new BlogDTO();
        blogDTO1.setId(1L);
        BlogDTO blogDTO2 = new BlogDTO();
        assertThat(blogDTO1).isNotEqualTo(blogDTO2);
        blogDTO2.setId(blogDTO1.getId());
        assertThat(blogDTO1).isEqualTo(blogDTO2);
        blogDTO2.setId(2L);
        assertThat(blogDTO1).isNotEqualTo(blogDTO2);
        blogDTO1.setId(null);
        assertThat(blogDTO1).isNotEqualTo(blogDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(blogMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(blogMapper.fromId(null)).isNull();
    }
}
