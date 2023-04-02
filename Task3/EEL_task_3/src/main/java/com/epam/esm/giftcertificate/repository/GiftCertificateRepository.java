package com.epam.esm.giftcertificate.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.epam.esm.utils.AppQuery.GiftCertificate.GET_CERTIFICATES_PRICE;


@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {
    Page<GiftCertificate> findByNameContaining(String partialName, Pageable pageable);

    Page<GiftCertificate> findByDescriptionContaining(String partialDescription, Pageable pageable);

    Page<GiftCertificate> findByTagsName(String tagName, Pageable pageable);

    Page<GiftCertificate> findByTagsIdIn(List<Long> tags, Pageable pageable);
    @Query(nativeQuery = true, value = GET_CERTIFICATES_PRICE)
    BigDecimal getPriceById(Long id);
}
