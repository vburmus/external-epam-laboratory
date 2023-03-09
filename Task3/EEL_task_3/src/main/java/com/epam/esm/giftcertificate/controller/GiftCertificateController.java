package com.epam.esm.giftcertificate.controller;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.service.GiftCertificateService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.epam.esm.utils.Constants.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/certificate")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public ResponseEntity<?> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getAllGiftCertificates(page, size)), HttpStatus.OK);
    }

    @GetMapping("/search/byId/{id}")
    public EntityModel<GiftCertificate> getById(@PathVariable("id") long id) {
        GiftCertificate gc = giftCertificateService.getCertificateById(id);
        Link selfLink = linkTo(methodOn(GiftCertificateController.class).getById(id)).withSelfRel();
        gc.add(selfLink);
        return EntityModel.of(gc);
    }

    @GetMapping("/search/byTag/{name}")
    public ResponseEntity<?> getCertificatesByTagName(@PathVariable("name") String name, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getGiftCertificatesByTagName(name, page, size)), HttpStatus.OK);
    }

    @GetMapping("/sort/byPart/{nameOrDescriptionPart}")
    public ResponseEntity<?> getCertificatesByPartOfNameOrDescription(@PathVariable("nameOrDescriptionPart") String nameOrDescriptionPart, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getGiftCertificatesByPart(nameOrDescriptionPart, page, size)), HttpStatus.OK);
    }

    @GetMapping("/sort/byDate/{direction}")
    public ResponseEntity<?> getCertificatesSortedByDate(@PathVariable("direction") String direction, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByDate(direction, page, size)), HttpStatus.OK);
    }

    @GetMapping("/sort/byName/{direction}")
    public ResponseEntity<?> getCertificatesSortedByName(@PathVariable("direction") String direction, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByName(direction, page, size)), HttpStatus.OK);
    }

    @GetMapping("/sort/byDateAndName/{directionDate}/{directionName}")
    public ResponseEntity<?> getCertificatesSortedByDateName(@PathVariable("directionDate") String directionDate, @PathVariable("directionName") String directionName, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByDateName(directionDate, directionName, page, size)), HttpStatus.OK);
    }

    @GetMapping({"/search/byTags"})
    public ResponseEntity<?> searchForCertificatesBySeveralTags(@RequestParam("tags") List<Long> tags, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesBySeveralTags(tags, page, size)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCertificate(@RequestBody GiftCertificate giftCertificate) {
        return new ResponseEntity<>(Map.of(GIFT_CERTIFICATE, giftCertificateService.createCertificate(giftCertificate)), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@PathVariable("id") long id, @RequestBody GiftCertificate giftCertificate) {
        return new ResponseEntity<>(Map.of(RESULT, giftCertificateService.updateCertificate(id, giftCertificate)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable("id") long id) {
        return new ResponseEntity<>(Map.of(RESULT, giftCertificateService.deleteCertificate(id)), HttpStatus.OK);
    }


}

