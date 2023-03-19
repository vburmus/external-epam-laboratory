package com.epam.esm.giftcertificate.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.direction.DirectionEnum;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;

    public static final String DONT_EXIST = "don't exist";
    public static final String IS_INVALID = " is invalid";


    public static final String EMPTY_MESSAGE = "Gift certificates are empty!";

    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository, TagService tagService) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
    }

    @Transactional
    public GiftCertificate createCertificate(GiftCertificate giftCertificate) {
        if (giftCertificateRepository.isGiftCertificateExist(giftCertificate))
            throw new ObjectAlreadyExistsException("Gift certificate with name = " + giftCertificate.getName() + ", duration = " + giftCertificate.getDuration() + " already exists");

        if (!ParamsValidation.isValidCertificate(giftCertificate))
            throw new ObjectIsInvalidException("Gift certificate with name = " + giftCertificate.getName() + ", duration = " + giftCertificate.getDuration() + "is invalid, please check your params");


        giftCertificateRepository.createGiftCertificate(giftCertificate);
        List<Long> listTagsId = tagService.getTagsIds(giftCertificate.getTags());
        giftCertificateRepository.createTagDependenciesForGiftCertificate(listTagsId, giftCertificateRepository.getGiftCertificatesID(giftCertificate));

        return getCertificateById(giftCertificateRepository.getGiftCertificatesID(giftCertificate));

    }

    public boolean deleteCertificate(long id) {
        return giftCertificateRepository.deleteGiftCertificate(id);
    }

    public List<GiftCertificate> getAllGiftCertificates(Integer page, Integer size) {
        return ParamsValidation.isEmptyOrElseThrowPageException(setTagsInCertificates(giftCertificateRepository.getAllGiftCertificates(page, size)));
    }

    @Transactional
    public boolean updateCertificate(long id, GiftCertificate giftCertificate) {
        Optional<Map<String, String>> updatingMap = ParamsValidation.isPatchCertificateValid(giftCertificate);

        List<Tag> tagsToUpdate = giftCertificate.getTags();
        if (updatingMap.isEmpty()) {
            throw new ObjectIsInvalidException("Update params are empty!");
        }
        if (tagsToUpdate == null) return giftCertificateRepository.updateGiftCertificate(id, updatingMap.get());
        else {
            if (ParamsValidation.isCertificateHaveValidTags(tagsToUpdate) && giftCertificateRepository.updateGiftCertificate(id, updatingMap.get())) {
                List<Tag> alreadyUsedTags = giftCertificateRepository.getAllTagsIdByCertificateId(id);
                List<Tag> tagsToDelete;

                List<String> tagsToUpdateNames = tagsToUpdate.stream().map(Tag::getName).collect(Collectors.toList());
                List<String> alreadyUsedTagNames = alreadyUsedTags.stream().map(Tag::getName).toList();
                List<String> tagsToDeleteNames = new ArrayList<>(alreadyUsedTagNames);
                tagsToDeleteNames.removeAll(tagsToUpdateNames);
                tagsToUpdateNames.removeAll(alreadyUsedTagNames);

                if (!tagsToDeleteNames.isEmpty()) {
                    tagsToDelete = tagService.getTagsByNames(tagsToDeleteNames);
                    giftCertificateRepository.deleteTagDependenciesForGiftCertificate(tagsToDelete.stream().map(Tag::getId).collect(Collectors.toList()), id);
                    alreadyUsedTags.removeAll(tagsToDelete);
                }
                if (!tagsToUpdateNames.isEmpty()) {
                    tagsToUpdate = tagsToUpdate.stream().filter(tag -> tagsToUpdateNames.contains(tag.getName())).collect(Collectors.toList());
                    tagService.isTagsExistOrElseCreate(tagsToUpdate);
                    tagsToUpdate = tagService.getTagsByNames(tagsToUpdateNames);
                    giftCertificateRepository.createTagDependenciesForGiftCertificate(tagsToUpdate.stream().map(Tag::getId).collect(Collectors.toList()), id);
                }
                return true;

            }
            throw new ObjectIsInvalidException("Some tags are invalid");
        }
    }

    public GiftCertificate getCertificateById(long id) {

        GiftCertificate giftCertificate = giftCertificateRepository.getGiftCertificateByID(id);
        if (giftCertificate == null) {
            throw new NoSuchItemException("GiftCertificate with id = " + id + " doesn't exist");
        }
        giftCertificate.setTags(getAllTagsByCertificate(giftCertificate));
        return giftCertificate;

    }

    public List<GiftCertificate> setTagsInCertificates(List<GiftCertificate> gcs) {
        for (GiftCertificate giftCertificate : gcs) {
            giftCertificate.setTags(getAllTagsByCertificate(giftCertificate));
        }
        return gcs;
    }

    public List<Tag> getAllTagsByCertificate(GiftCertificate gc) {
        return tagService.getAllTagsByCertificateID(giftCertificateRepository.getGiftCertificatesID(gc));
    }

    public List<GiftCertificate> getGiftCertificatesByTagName(String tagName, Integer page, Integer size) {
        if (tagName != null && !tagName.isEmpty()) {
            if (tagService.isTagWithNameExists(tagName)) {
                List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(giftCertificateRepository.getGiftCertificatesByTagName(tagName, page, size), "Gift Certificates with tag " + tagName + DONT_EXIST);
                return setTagsInCertificates(gc);
            }
            throw new NoSuchItemException("Tag with name " + tagName + " doesn't exist");
        }
        throw new ObjectIsInvalidException("Tag name " + tagName + IS_INVALID);
    }

    public List<GiftCertificate> getGiftCertificatesByPart(String part, Integer page, Integer size) {
        if (part != null && !part.isEmpty()) {
            List<GiftCertificate> certificates = giftCertificateRepository.getGiftCertificatesByPartOfDescription(part, page, size);
            if (certificates.isEmpty()) {
                List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(giftCertificateRepository.getGiftCertificatesByPartOfName(part, page, size), "Gift Certificates with part of name -> " + part + DONT_EXIST);
                return setTagsInCertificates(gc);
            }
            List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(certificates, "Gift Certificates with part of description -> " + part + DONT_EXIST);
            return setTagsInCertificates(gc);
        }
        throw new ObjectIsInvalidException("Part of description -> " + part + IS_INVALID);
    }


    public List<GiftCertificate> getCertificatesSortedByDate(DirectionEnum direction, Integer page, Integer size) {
        List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(giftCertificateRepository.getCertificatesSortedByDate(direction, page, size), EMPTY_MESSAGE);
        return setTagsInCertificates(gc);
    }


    public List<GiftCertificate> getCertificatesSortedByName(DirectionEnum direction, Integer page, Integer size) {
        List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(giftCertificateRepository.getCertificatesSortedByName(direction, page, size), EMPTY_MESSAGE);
        return setTagsInCertificates(gc);
    }


    public List<GiftCertificate> getCertificatesSortedByDateName(DirectionEnum directionDate, DirectionEnum directionName, Integer page, Integer size) {
        List<GiftCertificate> gc = ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(giftCertificateRepository.getCertificatesSortedByDateName(directionDate, directionName, page, size), EMPTY_MESSAGE);
        return setTagsInCertificates(gc);
    }

    public List<GiftCertificate> getCertificatesBySeveralTags(List<Long> tags, Integer page, Integer size) {
        return ParamsValidation.isEmptyOrElseThrowPageException(setTagsInCertificates(giftCertificateRepository.getCertificatesBySeveralTags(tags, page, size)));
    }
}
