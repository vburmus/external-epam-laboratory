package com.epam.esm.giftcertificate.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.giftcertificate.direction.DirectionEnum;

import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;

public interface GiftCertificateRepository {

    boolean createGiftCertificate(GiftCertificate giftCertificate);

    boolean createTagDependenciesForGiftCertificate(List<Long> tags, long giftCertificateID);


    List<GiftCertificate> getAllGiftCertificates(Integer page,Integer size);

    GiftCertificate getGiftCertificateByID(long id);

    boolean updateGiftCertificate(long id, Map<String, String> updatedCertificate);

    boolean deleteGiftCertificate(long id);

    boolean deleteTagDependenciesForGiftCertificate(List<Long> tags, long giftCertificateID);

    boolean isGiftCertificateExist(GiftCertificate giftCertificate);

    long getGiftCertificatesID(GiftCertificate giftCertificate);

    List<Tag> getAllTagsIdByCertificateId(long id);

    List<GiftCertificate> getGiftCertificatesByTagName(String tagName,Integer page,Integer size);

    List<GiftCertificate> getGiftCertificatesByPartOfName(String part,Integer page,Integer size);

    List<GiftCertificate> getGiftCertificatesByPartOfDescription(String part,Integer page,Integer size);

    List<GiftCertificate> getCertificatesSortedByDate(DirectionEnum direction,Integer page,Integer size);

    List<GiftCertificate> getCertificatesSortedByName(DirectionEnum direction,Integer page,Integer size);

    List<GiftCertificate> getCertificatesSortedByDateName(DirectionEnum directionDate, DirectionEnum directionName,Integer page,Integer size);

    List<GiftCertificate> getCertificatesBySeveralTags(List<Tag> tags,Integer page,Integer size);
}
