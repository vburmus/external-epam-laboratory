package com.epam.esm.giftcertificate.controller;


import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.service.GiftCertificateService;
import org.hibernate.persister.walking.internal.StandardAnyTypeDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/certificate")
public class GiftCertificateController {
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
}

