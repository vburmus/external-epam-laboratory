package com.epam.esm.tag.repository;

import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.AppQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByName(String tagName);

    @Query(nativeQuery = true, value = AppQuery.Tag.GET_MOST_USED_TAG)
    Tag getMostUsedTag();
}
