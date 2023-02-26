package com.epam.esm.giftcertificate.controller;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.service.GiftCertificateService;

import com.epam.esm.tag.model.Tag;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/certificate")
public class GiftCertificateController {

    public static final String GIFT_CERTIFICATES = "gift certificates";
    private final GiftCertificateService giftCertificateService;


    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;

    }

    @PostMapping
    public ResponseEntity<?> createCertificate(@RequestBody GiftCertificate giftCertificate) {
        return new ResponseEntity<>(Map.of("Certificate:",  giftCertificateService.createCertificate(giftCertificate)), HttpStatus.CREATED);
    }

    @GetMapping({"/{page}",""})
    public ResponseEntity<?> showAll(@PathVariable(required = false) Optional<Integer> page) {
        return ResponseEntity.ok(Map.of("gift certificate", giftCertificateService.getAllGiftCertificates(ParamsValidation.isValidPage(page))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable("id") long id) {
        HttpStatus status;
        if(giftCertificateService.deleteCertificate(id)) status = HttpStatus.OK;
        else status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(Map.of("status",status),status);
    }

    @GetMapping( "/search/byId{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(giftCertificateService.getCertificateById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@PathVariable("id") long id, @RequestBody GiftCertificate giftCertificate) {
        return ResponseEntity.ok(Map.of("result", giftCertificateService.updateCertificate(id, giftCertificate)));
    }

    @GetMapping({"/search/byTag/{name}","/search/byTag/{name}/{page}"})
    public ResponseEntity<?> getCertificatesByTagName(@PathVariable("name") String name, @PathVariable(required = false) Optional<Integer> page) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATES, giftCertificateService.getGiftCertificatesByTagName(name,ParamsValidation.isValidPage(page))));

    }

    @GetMapping({"/sort/byPart/{nameOrDescriptionPart}","/sort/byPart/{nameOrDescriptionPart}/{page}"})
    public ResponseEntity<?> getCertificateByPartOfNameOrDescription(@PathVariable("nameOrDescriptionPart") String nameOrDescriptionPart, @PathVariable(required = false) Optional<Integer> page) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATES, giftCertificateService.getGiftCertificatesByPart(nameOrDescriptionPart, ParamsValidation.isValidPage(page))));
    }

    @GetMapping({"/sort/byDate/{direction}","/sort/byDate/{direction}/{page}"})
    public ResponseEntity<?> getCertificatesSortedByDate(@PathVariable("direction") String direction,@PathVariable(required = false) Optional<Integer> page) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATES, giftCertificateService.getCertificatesSortedByDate(direction, ParamsValidation.isValidPage(page))));

    }

    @GetMapping({"/sort/byName/{direction}","/sort/byName/{direction}/{page}"})
    public ResponseEntity<?> getCertificatesSortedByName(@PathVariable("direction") String direction,@PathVariable(required = false) Optional<Integer> page) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATES, giftCertificateService.getCertificatesSortedByName(direction, ParamsValidation.isValidPage(page))));

    }

    @GetMapping({"/sort/byDateAndName/{directionDate}/{directionName}","/sort/byDateAndName/{directionDate}/{directionName}/{page}"})
    public ResponseEntity<?> getCertificatesSortedByDateName(@PathVariable("directionDate") String directionDate, @PathVariable("directionName") String directionName,@PathVariable(required = false) Optional<Integer> page) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATES, giftCertificateService.getCertificatesSortedByDateName(directionDate, directionName, ParamsValidation.isValidPage(page))));

    }
    @PostMapping({"/search/byTags","/search/byTags/{page}"})
    public ResponseEntity<?> searchForCertificatesBySeveralTags(@RequestBody List<Tag> tags,@PathVariable(required = false) Optional<Integer> page){
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATES, giftCertificateService.getCertificatesBySeveralTags(tags, ParamsValidation.isValidPage(page))));
    }
}

