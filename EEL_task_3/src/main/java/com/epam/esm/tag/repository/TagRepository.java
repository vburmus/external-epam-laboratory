package com.epam.esm.tag.repository;

import com.epam.esm.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    String GET_MOST_USED_TAG = "select tag.id,tag.name from (select user_id, tag_id, count(*) as tag_count from gift_certificate_has_tag " +
            "  join gift_certificate_has_order on gift_certificate_has_order.gift_certificate_id = gift_certificate_has_tag" +
            ".gift_certificate_id " +
            "  join purchase on purchase.id = gift_certificate_has_order.order_id where purchase.user_id = ( select user_id from purchase" +
            "  order by cost desc limit 1 ) group by user_id, tag_id order by tag_count desc limit 1) subquery join tag on tag.id = " +
            "subquery.tag_id;";


    boolean existsByName(String tagName);

    @Query(nativeQuery = true, value = GET_MOST_USED_TAG)
    Optional<Tag> getMostUsedTag();
}
