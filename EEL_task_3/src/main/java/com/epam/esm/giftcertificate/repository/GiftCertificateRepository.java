package com.epam.esm.giftcertificate.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.epam.esm.utils.Constants.GET_GC_BY_TAGS_AND_PART;


@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    Page<GiftCertificate> findByNameContaining(String partialName, Pageable pageable);

    Page<GiftCertificate> findByShortDescriptionContaining(String partialDescription, Pageable pageable);

    Page<GiftCertificate> findByTagsName(String tagName, Pageable pageable);

    Page<GiftCertificate> findByTagsIdIn(List<Long> tags, Pageable pageable);
    @Query(nativeQuery = true, value = GET_GC_BY_TAGS_AND_PART)
    Page<GiftCertificate> findByTagsIdInAndDescriptionOrNameContaining(List<Long> tags,String partial,
                                                                 Pageable pageable);
}