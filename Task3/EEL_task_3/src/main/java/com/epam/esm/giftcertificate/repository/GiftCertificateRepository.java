package com.epam.esm.giftcertificate.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;

import java.util.List;
import java.util.Map;

public interface GiftCertificateRepository {

    boolean createGiftCertificate(GiftCertificate giftCertificate);
    boolean createTagDependenciesForGiftCertificate(List<Long> tags, long giftCertificateID);


    List<GiftCertificate> getAllGiftCertificates();
    GiftCertificate getGiftCertificateByID(long id);

    boolean updateGiftCertificate(long id, Map<String,String> updatedCertificate);

    boolean deleteGiftCertificate(long id);
    boolean deleteTagDependenciesForGiftCertificate(List<Long> tags, long giftCertificateID);


    boolean isGiftCertificateExist(GiftCertificate giftCertificate);
    long getGiftCertificatesID(GiftCertificate giftCertificate);

    List<Tag> getAllTagsIdByCertificateId(long id);
}
