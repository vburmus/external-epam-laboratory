package com.epam.esm.giftcertificate.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;

    @Autowired
    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository, TagService tagService) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
    }

    @Transactional
    public boolean createCertificate(GiftCertificate giftCertificate) {
        if (!giftCertificateRepository.isGiftCertificateExist(giftCertificate)) {
            if (ParamsValidation.isValidCertificate(giftCertificate)) {
                giftCertificateRepository.createGiftCertificate(giftCertificate);
                List<Long> listTagsId = tagService.getTagsIds(giftCertificate.getTags());
                giftCertificateRepository.createTagDependenciesForGiftCertificate(listTagsId, giftCertificateRepository.getGiftCertificatesID(giftCertificate));
                return true;
            }
            throw new ObjectIsInvalidException("Gift certificate with id = " + giftCertificate.getName() + ", duration = " + giftCertificate.getDuration() + "is invalid, please check your params");
        } else
            throw new ObjectAlreadyExistsException("Gift certificate with id = " + giftCertificate.getName() + ", duration = " + giftCertificate.getDuration() + " already exists");

    }

    public boolean deleteCertificate(long id) {
        if (giftCertificateRepository.deleteGiftCertificate(id)) return true;
        else throw new NoSuchItemException("Gift certificate with id =" + id + "doesn't exist");

    }

    public List<GiftCertificate> getAllGiftCertificates() {
        return giftCertificateRepository.getAllGiftCertificates();
    }

    @Transactional
    public boolean updateCertificate(long id, GiftCertificate giftCertificate) {
        Optional<Map<String, String>> updatingMap = ParamsValidation.isPatchCertificateValid(giftCertificate);
        if (updatingMap.isPresent()) {
            List<Tag> tagsToUpdate = giftCertificate.getTags();
            if (tagsToUpdate == null) return giftCertificateRepository.updateGiftCertificate(id, updatingMap.get());
            else {
                if (ParamsValidation.isCertificateHaveValidTags(tagsToUpdate)) {
                    if (giftCertificateRepository.updateGiftCertificate(id, updatingMap.get())) {
                        List<Tag> alreadyUsedTags = giftCertificateRepository.getAllTagsIdByCertificateId(id);
                        List<Tag> tagsToDelete;

                        List<String> tagsToUpdateNames = tagsToUpdate.stream().map(Tag::getName).collect(Collectors.toList());
                        List<String> alreadyUsedTagNames = alreadyUsedTags.stream().map(Tag::getName).collect(Collectors.toList());
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
                }
                throw new ObjectIsInvalidException("Some tags from : " + Arrays.toString(tagsToUpdate.stream().map(Tag::getName).collect(Collectors.toList()).toArray()) + " are invalid");
            }
        }
        throw new ObjectIsInvalidException("Check your params for gift certificate with id = " + id);
    }

    public GiftCertificate getCertificateById(long id) {

        GiftCertificate giftCertificate = giftCertificateRepository.getGiftCertificateByID(id);
        if (giftCertificate == null)
            throw new NoSuchItemException("GiftCertificate with id = " + id + " doesn't exist");
        return giftCertificate;

    }

}
