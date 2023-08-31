package com.epam.esm.tag.repository;

import com.epam.esm.tag.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.epam.esm.utils.Constants.GET_MOST_USED_TAG;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String tagName);

    Page<Tag> getTagsByNameContaining(String namePart, Pageable pageable);

    @Query(nativeQuery = true, value = GET_MOST_USED_TAG)
    Optional<Tag> getMostUsedTag();
}