package com.epam.esm.giftcertificate.service;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectAlreadyExistsException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.model.GiftCertificateDTO;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.model.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import com.epam.esm.utils.mappers.EntityToDtoMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.epam.esm.utils.Constants.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
public class GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;
    private final EntityToDtoMapper entityToDtoMapper;

    public static final String IS_INVALID = " is invalid";

    public static final String SORT_REGEX = "^(-)?date$|^(-)?name$";


    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository, TagService tagService, EntityToDtoMapper entityToDtoMapper) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
        this.entityToDtoMapper = entityToDtoMapper;
    }

    @Transactional
    public GiftCertificateDTO createCertificate(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = entityToDtoMapper.toGiftCertificate(giftCertificateDTO);
        ExampleMatcher gcMatcher = ExampleMatcher.matching()
                .withIgnorePaths(CREATE_DATE, LAST_UPDATE_DATE, ID, TAGS, PRICE)
                .withMatcher(NAME, exact())
                .withMatcher(DESCRIPTION, exact())
                .withMatcher(DURATION, exact());
        Example<GiftCertificate> providedGC = Example.of(giftCertificate, gcMatcher);

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

        GiftCertificate gcResult = giftCertificateRepository.save(giftCertificate);
        return entityToDtoMapper.toGiftCertificateDTO(gcResult);
    }

    @Modifying
    public boolean deleteCertificate(Long id) {
        if (!giftCertificateRepository.existsById(id)) throw new NoSuchItemException("There is no gc with id= " + id);

        giftCertificateRepository.deleteById(id);
        return true;
    }

    public Page<GiftCertificateDTO> getAllGiftCertificates(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ParamsValidation.isListIsNotEmptyOrElseThrowNoSuchItem(giftCertificateRepository.findAll(pageRequest)).map(entityToDtoMapper::toGiftCertificateDTO);
    }

    public GiftCertificateDTO getCertificateById(long id) {

        Optional<GiftCertificate> giftCertificate = giftCertificateRepository.findById(id);
        if (giftCertificate.isEmpty()) {
            throw new NoSuchItemException("GiftCertificate with id = " + id + " doesn't exist");
        }
        return entityToDtoMapper.toGiftCertificateDTO(giftCertificate.get());
    }


    @Transactional
    @Modifying
    public GiftCertificateDTO updateCertificate(long id, GiftCertificateDTO updatedGiftCertificateDTO) {
        GiftCertificate updatedGiftCertificate = entityToDtoMapper.toGiftCertificate(updatedGiftCertificateDTO);
        Optional<GiftCertificate> gc = giftCertificateRepository.findById(id);
        if (gc.isEmpty()) throw new NoSuchItemException("No gift certificate with id = " + id);

        GiftCertificate giftCertificateFromDB = gc.get();
        if (ParamsValidation.ifCertificateNeedsAnUpdate(updatedGiftCertificate, giftCertificateFromDB)) {
            Map<String, String> updatingMap = ParamsValidation.isPatchCertificateValid(updatedGiftCertificate);

            if (updatingMap.containsKey(NAME)) giftCertificateFromDB.setName(updatingMap.get(NAME));
            if (updatingMap.containsKey(DESCRIPTION))
                giftCertificateFromDB.setDescription(updatingMap.get(DESCRIPTION));
            if (updatingMap.containsKey(PRICE))
                giftCertificateFromDB.setPrice(new BigDecimal(updatingMap.get(PRICE)));
            if (updatingMap.containsKey(DURATION))
                giftCertificateFromDB.setDuration(Integer.valueOf(updatingMap.get(DURATION)));
            giftCertificateFromDB.setLastUpdateDate(LocalDateTime.now());

            List<Tag> tags = tagService.checkTagsAndSaveIfDontExist(updatedGiftCertificate);
            giftCertificateFromDB.setTags(tags);
            GiftCertificate gcResult = giftCertificateRepository.save(giftCertificateFromDB);
            return entityToDtoMapper.toGiftCertificateDTO(gcResult);
        }
        return entityToDtoMapper.toGiftCertificateDTO(giftCertificateFromDB);
    }


    public Page<GiftCertificateDTO> getGiftCertificatesByTagName(String tagName, Integer page, Integer size) {

        if (tagName == null || tagName.isEmpty())
            throw new ObjectIsInvalidException("Tag name " + tagName + IS_INVALID);
        if (!tagService.existsByName(tagName))
            throw new NoSuchItemException("Tag with name " + tagName + " doesn't exist");

        PageRequest pageRequest = PageRequest.of(--page, size);
        return ParamsValidation.isListIsNotEmptyOrElseThrowNoSuchItem(giftCertificateRepository.findByTagsName(tagName, pageRequest)).map(entityToDtoMapper::toGiftCertificateDTO);
    }

    public Page<GiftCertificateDTO> getGiftCertificatesByPart(String part, Integer page, Integer size) {

        if (!ParamsValidation.isPartValidForSearch(part))
            throw new ObjectIsInvalidException("Part of description -> " + part + IS_INVALID);
        PageRequest pageRequest = PageRequest.of(--page, size);
        Page<GiftCertificate> certificates = giftCertificateRepository.findByNameContaining(part, pageRequest);
        if (!certificates.isEmpty())
            return ParamsValidation.isListIsNotEmptyOrElseThrowNoSuchItem(certificates).map(entityToDtoMapper::toGiftCertificateDTO);
        return ParamsValidation.isListIsNotEmptyOrElseThrowNoSuchItem(giftCertificateRepository.findByDescriptionContaining(part, pageRequest)).map(entityToDtoMapper::toGiftCertificateDTO);


    }


    public Page<GiftCertificateDTO> getCertificatesSortedByParam(String sortString, Integer page, Integer size) {

        String[] sort = sortString.split(",");
        Pattern sortPattern = Pattern.compile(SORT_REGEX);
        if (sort.length > 2) throw new ObjectIsInvalidException("To many params for sorting");

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
            return ParamsValidation.isListIsNotEmptyOrElseThrowNoSuchItem(giftCertificateRepository.findAll(pageRequest)).map(entityToDtoMapper::toGiftCertificateDTO);
        } else {
            String secondParam = sort[1];
            Sort.Direction secondDirection = ParamsValidation.getSortDirection(firstParam);
            secondParam = ParamsValidation.getSortParam(secondParam);
            Sort sortBy = Sort.by(new Sort.Order(firstDirection, firstParam), new Sort.Order(secondDirection, secondParam));
            PageRequest pageRequest = PageRequest.of(--page, size, sortBy);
            return ParamsValidation.isListIsNotEmptyOrElseThrowNoSuchItem(giftCertificateRepository.findAll(pageRequest)).map(entityToDtoMapper::toGiftCertificateDTO);

        }

    }

    public Page<GiftCertificateDTO> getCertificatesBySeveralTags(List<Long> tagsId, Integer page, Integer size) {

        PageRequest pageRequest = PageRequest.of(--page, size);
        return ParamsValidation.isListIsNotEmptyOrElseThrowNoSuchItem(giftCertificateRepository.findByTagsIdIn(tagsId, pageRequest)).map(entityToDtoMapper::toGiftCertificateDTO);
    }
}
