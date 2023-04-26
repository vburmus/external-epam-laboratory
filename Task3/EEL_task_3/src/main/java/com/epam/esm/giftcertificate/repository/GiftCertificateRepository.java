package com.epam.esm.giftcertificate.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    Page<GiftCertificate> findByNameContaining(String partialName, Pageable pageable);

    Page<GiftCertificate> findByDescriptionContaining(String partialDescription, Pageable pageable);

    Page<GiftCertificate> findByTagsName(String tagName, Pageable pageable);

    Page<GiftCertificate> findByTagsIdIn(List<Long> tags, Pageable pageable);
}
