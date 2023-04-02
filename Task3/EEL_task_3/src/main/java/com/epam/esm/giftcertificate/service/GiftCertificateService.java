package com.epam.esm.giftcertificate.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;


    public static final String IS_INVALID = " is invalid";

    public static final String SORT_REGEX = "^(-)?date$|^(-)?name$";


    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository, TagService tagService) {
        this.giftCertificateRepository = giftCertificateRepository;

        this.tagService = tagService;
    }

    @Transactional
    public GiftCertificate createCertificate(GiftCertificate giftCertificate) {

        Example<GiftCertificate> providedGC = Example.of(giftCertificate);
        if (giftCertificateRepository.exists(providedGC))
            throw new ObjectAlreadyExistsException("Gift certificate with name = " + giftCertificate.getName() + ", duration = " + giftCertificate.getDuration() + " already exists");
        if (!ParamsValidation.isValidCertificate(giftCertificate))
            throw new ObjectIsInvalidException("Gift certificate with name = " + giftCertificate.getName() + ", duration = " + giftCertificate.getDuration() + " is invalid, please check your params");
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        if (giftCertificate.getTags() != null) {
            List<Tag> tags = tagService.checkTagsAndSaveIfDontExist(giftCertificate);
            giftCertificate.setTags(tags);
        }
        giftCertificateRepository.save(giftCertificate);
        return giftCertificateRepository.findById(giftCertificate.getId())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve saved GiftCertificate"));
    }

    @Modifying
    public boolean deleteCertificate(long id) {
        if (giftCertificateRepository.existsById(id)) {
            giftCertificateRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Page<GiftCertificate> getAllGiftCertificates(Integer page, Integer size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(giftCertificateRepository.findAll(pageRequest));
    }

    public GiftCertificate getCertificateById(long id) {

        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        if (giftCertificate.isEmpty()) {
            throw new NoSuchItemException("GiftCertificate with id = " + id + " doesn't exist");
        }
        return giftCertificate.get();
    }


    @Transactional
    @Modifying
    public GiftCertificate updateCertificate(long id, GiftCertificate updatedGiftCertificate) {

        Optional<GiftCertificate> gc = giftCertificateRepository.findById(id);
        if (gc.isEmpty())
            throw new NoSuchItemException("No gift certificate with id = " + id);
        GiftCertificate giftCertificateFromDB = gc.get();
        if (ParamsValidation.ifCertificateNeedsAnUpdate(updatedGiftCertificate, giftCertificateFromDB)) {
            Map<String, String> updatingMap = ParamsValidation.isPatchCertificateValid(updatedGiftCertificate);

            if (updatingMap.containsKey("name"))
                giftCertificateFromDB.setName(updatingMap.get("name"));
            if (updatingMap.containsKey("description"))
                giftCertificateFromDB.setName(updatingMap.get("description"));
            if (updatingMap.containsKey("price"))
                giftCertificateFromDB.setName(updatingMap.get("price"));
            if (updatingMap.containsKey("duration"))
                giftCertificateFromDB.setName(updatingMap.get("duration"));
            giftCertificateFromDB.setLastUpdateDate(LocalDateTime.now());

            List<Tag> tags = tagService.checkTagsAndSaveIfDontExist(updatedGiftCertificate);
            giftCertificateFromDB.setTags(tags);


            return giftCertificateRepository.save(giftCertificateFromDB);
        }
        return giftCertificateFromDB;
    }


    public Page<GiftCertificate> getGiftCertificatesByTagName(String tagName, Integer page, Integer size) {
        page--;
        if (tagName != null && !tagName.isEmpty()) {
            if (tagService.existsByName(tagName)) {
                PageRequest pageRequest = PageRequest.of(page, size);
                return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(giftCertificateRepository.findByTagsName(tagName, pageRequest));
            }
            throw new NoSuchItemException("Tag with name " + tagName + " doesn't exist");
        }
        throw new ObjectIsInvalidException("Tag name " + tagName + IS_INVALID);
    }

    public Page<GiftCertificate> getGiftCertificatesByPart(String part, Integer page, Integer size) {
        page--;
        if (ParamsValidation.isPartValidForSearch(part)) {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<GiftCertificate> certificates = giftCertificateRepository.findByNameContaining(part, pageRequest);
            if (!certificates.isEmpty())
                return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(certificates);
            return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(giftCertificateRepository.findByDescriptionContaining(part, pageRequest));
        }
        throw new ObjectIsInvalidException("Part of description -> " + part + IS_INVALID);
    }


    public Page<GiftCertificate> getCertificatesSortedByParam(String sortString, Integer page, Integer size) {
        page--;
        String[] sort = sortString.split(",");
        Pattern sortPattern = Pattern.compile(SORT_REGEX);
        if (sort.length > 2)
            throw new ObjectIsInvalidException("To many params for sorting");

        for (String sortParam : sort) {
            if (!sortPattern.matcher(sortParam).find())
                throw new ObjectIsInvalidException("Some sort params are invalid.");
        }

        String firstParam = sort[0];
        Sort.Direction firstDirection = ParamsValidation.getSortDirection(firstParam);
        firstParam = ParamsValidation.getSortParam(firstParam);
        if (sort.length == 1) {
            Sort sortBy = Sort.by(firstDirection, firstParam);
            PageRequest pageRequest = PageRequest.of(page, size, sortBy);
            return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(giftCertificateRepository.findAll(pageRequest));
        } else {
            String secondParam = sort[1];
            Sort.Direction secondDirection = ParamsValidation.getSortDirection(firstParam);
            secondParam = ParamsValidation.getSortParam(secondParam);
            Sort sortBy = Sort.by(
                    new Sort.Order(firstDirection, firstParam),
                    new Sort.Order(secondDirection, secondParam));
            PageRequest pageRequest = PageRequest.of(page, size, sortBy);
            return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(giftCertificateRepository.findAll(pageRequest));

        }

    }

    public Page<GiftCertificate> getCertificatesBySeveralTags(List<Long> tagsId, Integer page, Integer size) {
        page--;
        PageRequest pageRequest = PageRequest.of(page, size);
        return ParamsValidation.isCertificatesArentEmptyOrElseThrowNoSuchItem(giftCertificateRepository.findByTagsIdIn(tagsId, pageRequest));
    }
}
