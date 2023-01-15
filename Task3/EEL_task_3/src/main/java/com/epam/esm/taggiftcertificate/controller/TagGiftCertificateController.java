package com.epam.esm.taggiftcertificate.controller;

import com.epam.esm.taggiftcertificate.service.TagGiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/tag-gift-certificate",produces = MediaType.APPLICATION_JSON_VALUE)
public class TagGiftCertificateController {
    private final TagGiftCertificateService tagGiftCertificateService;
    @Autowired
    public TagGiftCertificateController(TagGiftCertificateService tagGgiftCertificateService) {
        this.tagGiftCertificateService = tagGgiftCertificateService;
    }
    @GetMapping("/{name}")
    public ResponseEntity<?> getCertificateByTagsName(@PathVariable("name") String name){
        return ResponseEntity.ok(Map.of("gift certificates" , tagGiftCertificateService.getGiftCertificatesByTagName(name)));

    }

    @GetMapping("/{nameOrDescriptionPart}")
    public ResponseEntity<?> getCertificateByPartOfNameOrDescription(@PathVariable("nameOrDescriptionPart") String nameOrDescriptionPart){
        return ResponseEntity.ok(Map.of("gift certificates" , tagGiftCertificateService.getGiftCertificatesByPartOfDescription(nameOrDescriptionPart)));
    }
    @GetMapping("/sorted-by-date/{direction}")
    public ResponseEntity<?> getCertificatesSortedByDate(@PathVariable("direction") String direction){
        return ResponseEntity.ok(Map.of("gift certificates" , tagGiftCertificateService.getCertificatesSortedByDate(direction)));

    }
    @GetMapping("/sorted-by-name/{direction}")
    public ResponseEntity<?> getCertificatesSortedByName(@PathVariable("direction") String direction){
        return ResponseEntity.ok(Map.of("gift certificates" , tagGiftCertificateService.getCertificatesSortedByName(direction)));

    }
    @GetMapping("/sorted-by-date-and-name/{directionDate}/{directionName}")
    public ResponseEntity<?> getCertificatesSortedByDateName(@PathVariable("directionDate") String directionDate, @PathVariable("directionName") String directionName){
        return ResponseEntity.ok(Map.of("gift certificates" , tagGiftCertificateService.getCertificatesSortedByDateName(directionDate,directionName)));

    }


}
