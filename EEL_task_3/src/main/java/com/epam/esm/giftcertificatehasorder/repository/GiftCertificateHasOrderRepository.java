package com.epam.esm.giftcertificatehasorder.repository;

import com.epam.esm.giftcertificatehasorder.model.GiftCertificateHasOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftCertificateHasOrderRepository extends JpaRepository<GiftCertificateHasOrder, Long> {
}