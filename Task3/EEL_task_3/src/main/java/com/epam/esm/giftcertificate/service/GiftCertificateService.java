package com.epam.esm.giftcertificate.service;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.repository.GiftCertificateRepository;
import com.epam.esm.tag.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.ServerException;
import java.util.List;
import java.util.Map;

@Service
public class GiftCertificateService {
    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;
    //TODO
    @Autowired
    public GiftCertificateService(GiftCertificateRepository giftCertificateRepository, TagService tagService) {
        this.giftCertificateRepository = giftCertificateRepository;
        this.tagService = tagService;
    }
    @Transactional
    public boolean createCertificate(GiftCertificate giftCertificate) throws Exception {
        if (!giftCertificateRepository.isGiftCertificateExist(giftCertificate)) {
             boolean res = giftCertificateRepository.createGiftCertificate(giftCertificate);
             if(res) {
                 List<Long> listTagsId = tagService.getTagsIds(giftCertificate.getTags());
                 giftCertificateRepository.createTagDependenciesForGiftCertificate(listTagsId, giftCertificateRepository.getGiftCertificatesID(giftCertificate));
                 return res;
             }
             throw new Exception();
        } else
            throw new ServerException("Such certificate has already existed");

    }

    public boolean deleteCertificate(long id) throws Exception {
        if (giftCertificateRepository.deleteGiftCertificate(id))
            return true;
        else
            throw new Error("There is no such certificate");

    }
    public List<GiftCertificate> getAllGiftCertificates(){
        return giftCertificateRepository.getAllGiftCertificates();
    }
//TODO
    public boolean updateCertificate(long id, Map<String,String> updateMap) throws Exception {
        if(giftCertificateRepository.updateGiftCertificate(id,updateMap))
            return true;
        else
            throw new Error("There is no such certificate");

    }
    public GiftCertificate getCertificateById(long id) throws Exception {
        GiftCertificate giftCertificate = giftCertificateRepository.getGiftCertificateByID(id);

            return giftCertificate;
       }

}
