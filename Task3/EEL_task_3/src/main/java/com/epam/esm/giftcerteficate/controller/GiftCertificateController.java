package com.epam.esm.giftcerteficate.controller;


import com.epam.esm.giftcerteficate.model.GiftCertificate;

import com.epam.esm.giftcerteficate.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value ="/certificate",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping
    public ResponseEntity<?> createCertificate(@RequestBody GiftCertificate giftCertificate) throws Exception {
        giftCertificateService.createCertificate(giftCertificate);
        return new ResponseEntity<>(Map.of("status", HttpStatus.CREATED), HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<?> showAll(){;
        return ResponseEntity.ok(Map.of("gift certificate", giftCertificateService.getAllGiftCertificates()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable("id") long id) throws Exception {
        giftCertificateService.deleteCertificate(id);
        return new ResponseEntity<>(Map.of("status", HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) throws Exception {
            return ResponseEntity.ok(Map.of("gift certificate", giftCertificateService.getCertificateById(id)));
    }
    //TODO
    @PatchMapping("/{id}")
    public String updateCertificate(@PathVariable("id") long id) throws Exception {
       //TODO TODO
        return "redirect:/certificate";
    }


    }

