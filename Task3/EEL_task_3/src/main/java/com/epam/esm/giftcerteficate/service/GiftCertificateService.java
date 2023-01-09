package com.epam.esm.giftcerteficate.service;

import com.epam.esm.giftcerteficate.model.GiftCertificate;
import com.epam.esm.giftcerteficate.repository.GiftCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.ServerException;
@Service
public class GiftCertificateService {
    private GiftCertificateRepository giftCertificateRepository;
    //TODO
    @Autowired
    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository) {
        this.giftCertificateRepository = giftCertificateRepository;

    }
    /*@Transactional*/
    public boolean createCertificate(GiftCertificate giftCertificate) throws Exception {
        if (!giftCertificateRepository.isGiftCertificateExist(giftCertificate)) {

            boolean result = giftCertificateRepository.createGiftCertificate(giftCertificate);

           /* if (giftCertificate.getTags() != null) {
                tagService.isTagsExistOrElseCreate(giftCertificate.getTags());

                List<Long> listTagsId = tagService.getTagsIdByTags(giftCertificate.getTags());
                giftCertificateRepository.createGiftCertificateTagRelationship(listTagsId, getGiftCertificateId(giftCertificate));
            }*/

            return result;

        } else {
            throw new Exception("Such certificate has already existed");
        }
    }
}
