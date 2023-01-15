package com.epam.esm.taggiftcertificate.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.repository.TagRepository;
import com.epam.esm.taggiftcertificate.direction.DirectionEnum;
import com.epam.esm.taggiftcertificate.repository.TagGiftCertificateRepository;
import com.epam.esm.utils.AppQuery;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class TagGiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final TagGiftCertificateRepository tagGiftCertificateRepository;
    String EMPTY_MESSAGE = "Gift certificates are empty!";
    @Autowired
    public TagGiftCertificateService(GiftCertificateRepository giftCertificateRepository, TagRepository tagRepository, TagGiftCertificateRepository tagGiftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagRepository = tagRepository;
        this.tagGiftCertificateRepository = tagGiftCertificateRepository;
    }


    public List<GiftCertificate> getGiftCertificatesByTagName(String tagName) {
        if (!tagName.isEmpty()) {
            if (tagRepository.isTagExists(tagName)) {
                return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getGiftCertificatesByTagName(tagName), "Gift Certificates with tag " + tagName + "dont exist");
            }
            throw new NoSuchItemException("Tag with name " + tagName + " doesn't exist");
        }
        throw new ObjectIsInvalidException("Tag name " + tagName + " is invalid");
    }


    public List<GiftCertificate> getGiftCertificatesByPartOfName(String part) {
        if (!part.isEmpty()) {
            return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getGiftCertificatesByPartOfName(part), "Gift Certificates with part of name -> " + part + "dont exist");
        }
        throw new ObjectIsInvalidException("Part of name -> " + part + " is invalid");
    }

    public List<GiftCertificate> getGiftCertificatesByPartOfDescription(String part) {
        if (!part.isEmpty()) {
            List<GiftCertificate> certificates = tagGiftCertificateRepository.getGiftCertificatesByPartOfDescription(part);
            if(certificates.isEmpty()){
                return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getGiftCertificatesByPartOfName(part), "Gift Certificates with part of name -> " + part + "dont exist");
            }
            return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(certificates, "Gift Certificates with part of description -> " + part + "dont exist");

        }
        throw new ObjectIsInvalidException("Part of description -> " + part + " is invalid");
    }


    public List<GiftCertificate> getCertificatesSortedByDate(String direction) {

        if (ParamsValidation.isDirectionValid(direction)) {
            if(direction.toUpperCase(Locale.ROOT).equals(DirectionEnum.ASC))
                return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getCertificatesSortedByDate(DirectionEnum.ASC), EMPTY_MESSAGE);
            else
                return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getCertificatesSortedByDate(DirectionEnum.DESC), EMPTY_MESSAGE);
        }
        throw new ObjectIsInvalidException("Direction " + direction + " is invalid");
    }


    public List<GiftCertificate> getCertificatesSortedByName(String direction) {

        if (ParamsValidation.isDirectionValid(direction)) {
            if(direction.toUpperCase(Locale.ROOT).equals(DirectionEnum.ASC))
                return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getCertificatesSortedByName(DirectionEnum.ASC), EMPTY_MESSAGE);
            else
                return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getCertificatesSortedByName(DirectionEnum.DESC), EMPTY_MESSAGE);
        }
        throw new ObjectIsInvalidException("Direction " + direction + " is invalid");
    }


    public List<GiftCertificate> getCertificatesSortedByDateName(String directionDate, String directionName) {
        if (ParamsValidation.isDirectionValid(directionDate) && ParamsValidation.isDirectionValid(directionName)) {
           DirectionEnum direction1 = directionDate=="ASC"? DirectionEnum.ASC: DirectionEnum.DESC;
           DirectionEnum direction2 = directionName=="ASC"? DirectionEnum.ASC: DirectionEnum.DESC;
                return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(tagGiftCertificateRepository.getCertificatesSortedByDateName(direction1,direction2), EMPTY_MESSAGE);
        }
        throw new ObjectIsInvalidException("Some of directions: " + directionDate + " " + directionName + " are invalid");

    }
}
