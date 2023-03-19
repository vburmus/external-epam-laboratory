package com.epam.esm.giftcertificate.controller;

import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.direction.DirectionEnum;
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

    public static final String NAME_DESC = "-name";
    public static final String NAME_ASC = "name";
    public static final String DATE_ASC = "date";
    public static final String DATE_DESC = "-date";
    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public ResponseEntity<?> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getAllGiftCertificates(page, size)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public EntityModel<GiftCertificate> getById(@PathVariable("id") long id) {
        GiftCertificate gc = giftCertificateService.getCertificateById(id);
        Link selfLink = linkTo(methodOn(GiftCertificateController.class).getById(id)).withSelfRel();
        gc.add(selfLink);
        return EntityModel.of(gc);
    }

    @GetMapping("/search/by-tag")
    public ResponseEntity<?> getCertificatesByTagName(@RequestParam(NAME_ASC) String name, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getGiftCertificatesByTagName(name, page, size)), HttpStatus.OK);
    }

    @GetMapping("/search/by-part-name-description")
    public ResponseEntity<?> getCertificatesByPartOfNameOrDescription(@RequestParam("part") String nameOrDescriptionPart, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getGiftCertificatesByPart(nameOrDescriptionPart, page, size)), HttpStatus.OK);
    }
    @GetMapping("/sort")
    public ResponseEntity<?> getSortedCertificates(@RequestParam("sort") List<String> sort, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size){

        if(sort.size()==1) {
            String param = sort.get(0);
            if (param.equals(DATE_ASC))
                return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByDate(DirectionEnum.ASC, page, size)), HttpStatus.OK);
            if (param.equals(DATE_DESC))
                return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByDate(DirectionEnum.DESC, page, size)), HttpStatus.OK);
            if (param.equals(NAME_ASC))
                return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByName(DirectionEnum.ASC, page, size)), HttpStatus.OK);
            if (param.equals(NAME_DESC))
                return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByName(DirectionEnum.DESC, page, size)), HttpStatus.OK);
        }
        if(sort.size()==2){
            if (sort.contains(DATE_ASC)) {
                if (sort.contains(NAME_ASC))
                    return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByDateName(DirectionEnum.ASC, DirectionEnum.ASC, page, size)), HttpStatus.OK);
                if(sort.contains(NAME_DESC))
                    return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByDateName(DirectionEnum.ASC, DirectionEnum.DESC, page, size)), HttpStatus.OK);
            }

            if(sort.contains(DATE_DESC)){
                if (sort.contains(NAME_ASC))
                    return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByDateName(DirectionEnum.DESC, DirectionEnum.ASC, page, size)), HttpStatus.OK);
                if(sort.contains(NAME_DESC))
                    return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByDateName(DirectionEnum.DESC, DirectionEnum.DESC, page, size)), HttpStatus.OK);
            }
        }
        throw new ObjectIsInvalidException("Please, provide correct sort parameter");
        }

    @GetMapping({"/search/by-tags"})
    public ResponseEntity<?> searchForCertificatesBySeveralTags(@RequestParam("tagsIDs") List<Long> tagsIDs, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesBySeveralTags(tagsIDs, page, size)), HttpStatus.OK);
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

