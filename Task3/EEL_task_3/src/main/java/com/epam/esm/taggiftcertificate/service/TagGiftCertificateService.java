package com.epam.esm.taggiftcertificate.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.taggiftcertificate.direction.DirectionEnum;
import com.epam.esm.taggiftcertificate.repository.TagGiftCertificateRepository;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class TagGiftCertificateService {
    public static final String DONT_EXIST = "don't exist";
    public static final String IS_INVALID = " is invalid";
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final TagGiftCertificateRepository tagGiftCertificateRepository;
    public static final String EMPTY_MESSAGE = "Gift certificates are empty!";

    @Autowired
    public TagGiftCertificateService(GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository, TagGiftCertificateRepository tagGiftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.tagGiftCertificateRepository = tagGiftCertificateRepository;
    }


    private List<GiftCertificate> setTagsInCertificate(List<GiftCertificate> gc) {
        for (GiftCertificate giftCertificate : gc) {
            giftCertificate.setTags(tagRepository.getAllTagsByCertificateID(giftCertificateRepository.getGiftCertificatesID(giftCertificate)));
        }
        return gc;
    }

    public List<GiftCertificate> getGiftCertificatesByTagName(String tagName) {
        if (tagName != null && !tagName.isEmpty()) {
            if (tagRepository.isTagExists(tagName)) {
                List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getGiftCertificatesByTagName(tagName), "Gift Certificates with tag " + tagName + DONT_EXIST);
                return setTagsInCertificate(gc);
            }
            throw new NoSuchItemException("Tag with name " + tagName + " doesn't exist");
        }
        throw new ObjectIsInvalidException("Tag name " + tagName + IS_INVALID);
    }

    public List<GiftCertificate> getGiftCertificatesByPart(String part) {
        if (part != null && !part.isEmpty()) {
            List<GiftCertificate> certificates = tagGiftCertificateRepository.getGiftCertificatesByPartOfDescription(part);
            if (certificates.isEmpty()) {
                List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getGiftCertificatesByPartOfName(part), "Gift Certificates with part of name -> " + part + DONT_EXIST);
                return setTagsInCertificate(gc);
            }
            List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(certificates, "Gift Certificates with part of description -> " + part + DONT_EXIST);
            return setTagsInCertificate(gc);
        }
        throw new ObjectIsInvalidException("Part of description -> " + part + IS_INVALID);
    }


    public List<GiftCertificate> getCertificatesSortedByDate(String direction) {

        if (ParamsValidation.isDirectionValid(direction)) {
            if (direction.toUpperCase(Locale.ROOT).equals(DirectionEnum.ASC.name())) {
                List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getCertificatesSortedByDate(DirectionEnum.ASC), EMPTY_MESSAGE);
                return setTagsInCertificate(gc);
            }
            List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getCertificatesSortedByDate(DirectionEnum.DESC), EMPTY_MESSAGE);
            return setTagsInCertificate(gc);
        }
        throw new ObjectIsInvalidException("Direction " + direction + IS_INVALID);
    }


    public List<GiftCertificate> getCertificatesSortedByName(String direction) {

        if (ParamsValidation.isDirectionValid(direction)) {
            if (direction.toUpperCase(Locale.ROOT).equals(DirectionEnum.ASC.name())) {
                List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getCertificatesSortedByName(DirectionEnum.ASC), EMPTY_MESSAGE);
                return setTagsInCertificate(gc);
            }
            List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getCertificatesSortedByName(DirectionEnum.DESC), EMPTY_MESSAGE);
            return setTagsInCertificate(gc);
        }
        throw new ObjectIsInvalidException("Direction " + direction + IS_INVALID);
    }


    public List<GiftCertificate> getCertificatesSortedByDateName(String directionDate, String directionName) {
        if (ParamsValidation.isDirectionValid(directionDate) && ParamsValidation.isDirectionValid(directionName)) {
            DirectionEnum direction1 = directionDate.equals(DirectionEnum.ASC.name()) ? DirectionEnum.ASC : DirectionEnum.DESC;
            DirectionEnum direction2 = directionName.equals(DirectionEnum.ASC.name()) ? DirectionEnum.ASC : DirectionEnum.DESC;
            List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getCertificatesSortedByDateName(direction1, direction2), EMPTY_MESSAGE);
            return setTagsInCertificate(gc);
        }
        throw new ObjectIsInvalidException("Some of directions: " + directionDate + " " + directionName + " are invalid");

    }
}
