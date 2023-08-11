package com.epam.esm.tag.repository;

import com.epam.esm.tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    String GET_MOST_USED_TAG = "SELECT tag.id,tag.name FROM (SELECT user_id, tag_id, COUNT(*) AS tag_count FROM gift_certificate_has_tag " +
            "  JOIN gift_certificate_has_order ON gift_certificate_has_order.gift_certificate_id = gift_certificate_has_tag.gift_certificate_id " +
            "  JOIN purchase ON purchase.id = gift_certificate_has_order.order_id WHERE purchase.user_id = ( SELECT user_id FROM purchase " +
            "  ORDER BY cost DESC LIMIT 1 ) GROUP BY user_id, tag_id ORDER BY tag_count DESC LIMIT 1) subquery JOIN tag ON tag.id = subquery.tag_id;";


    boolean existsByName(String tagName);

    @Query(nativeQuery = true, value = GET_MOST_USED_TAG)
    Tag getMostUsedTag();
}
