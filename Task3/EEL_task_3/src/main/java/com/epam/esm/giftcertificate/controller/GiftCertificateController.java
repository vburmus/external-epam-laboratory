package com.epam.esm.giftcertificate.controller;


import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.service.GiftCertificateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping
    public ResponseEntity<?> showAll() {
        return ResponseEntity.ok(Map.of("gift certificate", giftCertificateService.getAllGiftCertificates()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable("id") long id) {
        HttpStatus status;
        if(giftCertificateService.deleteCertificate(id)) status = HttpStatus.OK;
        else status = HttpStatus.NOT_FOUND;

        return new ResponseEntity<>(Map.of("status",status),status);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(giftCertificateService.getCertificateById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@PathVariable("id") long id, @RequestBody GiftCertificate giftCertificate) {
        return ResponseEntity.ok(Map.of("result", giftCertificateService.updateCertificate(id, giftCertificate)));
    }

    @GetMapping("/byTag/{name}")
    public ResponseEntity<?> getCertificatesByTagName(@PathVariable("name") String name) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATES, giftCertificateService.getGiftCertificatesByTagName(name)));

    }

    @GetMapping("/sort/byPart/{nameOrDescriptionPart}")
    public ResponseEntity<?> getCertificateByPartOfNameOrDescription(@PathVariable("nameOrDescriptionPart") String nameOrDescriptionPart) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATES, giftCertificateService.getGiftCertificatesByPart(nameOrDescriptionPart)));
    }

    @GetMapping("/sort/byDate/{direction}")
    public ResponseEntity<?> getCertificatesSortedByDate(@PathVariable("direction") String direction) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATES, giftCertificateService.getCertificatesSortedByDate(direction)));

    }

    @GetMapping("/sort/byName/{direction}")
    public ResponseEntity<?> getCertificatesSortedByName(@PathVariable("direction") String direction) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATES, giftCertificateService.getCertificatesSortedByName(direction)));

    }

    @GetMapping("/sort/byDateAndName/{directionDate}/{directionName}")
    public ResponseEntity<?> getCertificatesSortedByDateName(@PathVariable("directionDate") String directionDate, @PathVariable("directionName") String directionName) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATES, giftCertificateService.getCertificatesSortedByDateName(directionDate, directionName)));

    }
}

