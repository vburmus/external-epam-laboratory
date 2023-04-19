package com.epam.esm.giftcertificate.controller;

import com.epam.esm.giftcertificate.model.GiftCertificateDTO;
import com.epam.esm.giftcertificate.service.GiftCertificateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.epam.esm.utils.Constants.*;

@RestController
@RequestMapping(value = "/certificate")
@Profile("default")
public class GiftCertificateController {


    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping
    public ResponseEntity<?> createCertificate(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        return new ResponseEntity<>(Map.of(GIFT_CERTIFICATE, giftCertificateService.createCertificate(giftCertificateDTO)),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                     @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getAllGiftCertificates(page, size)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATE, giftCertificateService.getCertificateById(id)));
    }


    @GetMapping("/search/by-tag")
    public ResponseEntity<?> getCertificatesByTagName(@RequestParam("name") String name,
                                                      @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                      @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getGiftCertificatesByTagName(name, page, size)), HttpStatus.OK);
    }

    @GetMapping("/search/by-part-name-description")
    public ResponseEntity<?> getCertificatesByPart(@RequestParam("part") String nameOrDescriptionPart,
                                                   @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                   @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getGiftCertificatesByPart(nameOrDescriptionPart, page, size)),
                HttpStatus.OK);
    }


    @GetMapping("/sort")
    public ResponseEntity<?> getSortedCertificates(@Parameter(description = SORT_DESCTIPTION) @RequestParam("sort") String sortString,
                                                   @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                   @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByParam(sortString, page, size)),
                HttpStatus.OK);
    }

    @GetMapping({"/search/by-tags"})
    public ResponseEntity<?> searchForCertificatesBySeveralTags(@RequestParam("tagsId") List<Long> tagsId,
                                                                @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                                @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesBySeveralTags(tagsId, page, size)),
                HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<?> updateCertificate(@PathVariable("id") long id,
                                               @RequestBody JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        return new ResponseEntity<>(Map.of(RESULT, giftCertificateService.updateCertificate(id, patch)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of(RESULT, giftCertificateService.deleteCertificate(id)), HttpStatus.OK);
    }


}

