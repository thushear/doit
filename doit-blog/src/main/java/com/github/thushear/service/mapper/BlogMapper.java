package com.github.thushear.service.mapper;

import com.github.thushear.domain.*;
import com.github.thushear.service.dto.BlogDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Blog and its DTO BlogDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BlogMapper extends EntityMapper<BlogDTO, Blog> {

    

    

    default Blog fromId(Long id) {
        if (id == null) {
            return null;
        }
        Blog blog = new Blog();
        blog.setId(id);
        return blog;
    }
}
