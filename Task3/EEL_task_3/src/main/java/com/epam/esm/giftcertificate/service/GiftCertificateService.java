package com.epam.esm.giftcertificate.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.taggiftcertificate.repository.TagGiftCertificateRepository;
import com.epam.esm.taggiftcertificate.service.TagGiftCertificateService;
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
    private final TagGiftCertificateService tagGiftCertificateService;

    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository, TagService tagService, TagGiftCertificateRepository tagGiftCertificateRepository, TagGiftCertificateService tagGiftCertificateService) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
        this.tagGiftCertificateService = tagGiftCertificateService;
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
        //get created
        return getCertificateById(giftCertificateRepository.getGiftCertificatesID(giftCertificate));

    }

    public boolean deleteCertificate(long id) {

        if (giftCertificateRepository.deleteGiftCertificate(id)) return true;
        return false;

    }

    public List<GiftCertificate> getAllGiftCertificates() {
        return tagGiftCertificateService.setTagsInCertificates(giftCertificateRepository.getAllGiftCertificates());
    }

    @Transactional
    public boolean updateCertificate(long id, GiftCertificate giftCertificate) {
        Optional<Map<String, String>> updatingMap = ParamsValidation.isPatchCertificateValid(giftCertificate);

        List<Tag> tagsToUpdate = giftCertificate.getTags();
        if (tagsToUpdate == null) return giftCertificateRepository.updateGiftCertificate(id, updatingMap.get());
        else {
            if (ParamsValidation.isCertificateHaveValidTags(tagsToUpdate) && giftCertificateRepository.updateGiftCertificate(id, updatingMap.get())) {
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
            throw new ObjectIsInvalidException("Some tags are invalid");
        }
    }

    public GiftCertificate getCertificateById(long id) {

        GiftCertificate giftCertificate = giftCertificateRepository.getGiftCertificateByID(id);
        if (giftCertificate == null) {
            throw new NoSuchItemException("GiftCertificate with id = " + id + " doesn't exist");
        }
        giftCertificate.setTags(tagGiftCertificateService.getAllTagsByCertificate(giftCertificate));
        return giftCertificate;

    }

}
