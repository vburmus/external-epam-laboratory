package com.epam.esm.giftcertificate.controller;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.service.GiftCertificateService;
import com.epam.esm.utils.datavalidation.ParamsValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.epam.esm.utils.Constants.*;

@RestController
@RequestMapping(value = "/certificate")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping()
    public ResponseEntity<?> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return ParamsValidation.isNotFound(giftCertificateService.getAllGiftCertificates(page, size));
    }

    @GetMapping("/search/byId/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATE, giftCertificateService.getCertificateById(id)));
    }

    @GetMapping("/search/byTag/{name}")
    public ResponseEntity<?> getCertificatesByTagName(@PathVariable("name") String name, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return ParamsValidation.isNotFound(giftCertificateService.getGiftCertificatesByTagName(name, page, size));
    }

    @GetMapping("/sort/byPart/{nameOrDescriptionPart}")
    public ResponseEntity<?> getCertificatesByPartOfNameOrDescription(@PathVariable("nameOrDescriptionPart") String nameOrDescriptionPart, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return ParamsValidation.isNotFound(giftCertificateService.getGiftCertificatesByPart(nameOrDescriptionPart, page, size));
    }

    @GetMapping("/sort/byDate/{direction}")
    public ResponseEntity<?> getCertificatesSortedByDate(@PathVariable("direction") String direction, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return ParamsValidation.isNotFound(giftCertificateService.getCertificatesSortedByDate(direction, page, size));
    }

    @GetMapping("/sort/byName/{direction}")
    public ResponseEntity<?> getCertificatesSortedByName(@PathVariable("direction") String direction, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return ParamsValidation.isNotFound(giftCertificateService.getCertificatesSortedByName(direction, page, size));
    }

    @GetMapping("/sort/byDateAndName/{directionDate}/{directionName}")
    public ResponseEntity<?> getCertificatesSortedByDateName(@PathVariable("directionDate") String directionDate, @PathVariable("directionName") String directionName, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return ParamsValidation.isNotFound(giftCertificateService.getCertificatesSortedByDateName(directionDate, directionName, page, size));
    }

    @GetMapping({"/search/byTags"})
    public ResponseEntity<?> searchForCertificatesBySeveralTags(@RequestParam("tags") List<Long> tags, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return ParamsValidation.isNotFound(giftCertificateService.getCertificatesBySeveralTags(tags, page, size));
    }

    @PostMapping
    public ResponseEntity<?> createCertificate(@RequestBody GiftCertificate giftCertificate) {
        return new ResponseEntity<>(Map.of(GIFT_CERTIFICATE, giftCertificateService.createCertificate(giftCertificate)), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@PathVariable("id") long id, @RequestBody GiftCertificate giftCertificate) {
        return new ResponseEntity<>(Map.of(RESULT,giftCertificateService.updateCertificate(id, giftCertificate)),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of(RESULT,giftCertificateService.deleteCertificate(id)),HttpStatus.OK);
    }


}

