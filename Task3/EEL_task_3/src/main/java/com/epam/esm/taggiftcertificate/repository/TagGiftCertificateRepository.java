package com.epam.esm.taggiftcertificate.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.taggiftcertificate.direction.DirectionEnum;

import java.util.List;

public interface TagGiftCertificateRepository {
    List<GiftCertificate> getGiftCertificatesByTagName(String tagName);

    List<GiftCertificate> getGiftCertificatesByPartOfName(String part);

    List<GiftCertificate> getGiftCertificatesByPartOfDescription(String part);

    List<GiftCertificate> getCertificatesSortedByDate(DirectionEnum direction);

    List<GiftCertificate> getCertificatesSortedByName(DirectionEnum direction);

    List<GiftCertificate> getCertificatesSortedByDateName(DirectionEnum directionDate, DirectionEnum directionName);

}
