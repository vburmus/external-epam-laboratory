package com.epam.esm.giftcerteficate.repository;

import com.epam.esm.giftcerteficate.model.GiftCertificate;
import com.epam.esm.utils.AppQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface GiftCertificateRepository {

    boolean createGiftCertificate(GiftCertificate giftCertificate);
    boolean createTagDependencyForGiftCertificate(List<Long> tags, long giftCertificateID);


    List<GiftCertificate> getAllGiftCertificates();
    GiftCertificate getGiftCertificateByID(long id);

    boolean updateGiftCertificate(long id, GiftCertificate updatedCertificate);

    boolean deleteGiftCertificate(long id);
    boolean deleteTagDependencyForGiftCertificate(List<Long> tags, long giftCertificateID);


    boolean isGiftCertificateExist(GiftCertificate giftCertificate);
    long getGiftCertificatesID(GiftCertificate giftCertificate);

}
