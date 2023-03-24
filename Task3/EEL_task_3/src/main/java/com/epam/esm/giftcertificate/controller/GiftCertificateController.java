package com.epam.esm.giftcertificate.controller;

import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.direction.DirectionEnum;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.service.GiftCertificateService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.epam.esm.utils.Constants.*;

@RestController
@RequestMapping(value = "/certificate")
@Profile("default")
public class GiftCertificateController {

    public static final String NAME_REGEX = "^(-)?name$";
    public static final String DATE_REGEX = "^(-)?date$";
    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public ResponseEntity<?> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getAllGiftCertificates(page, size)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(Map.of(GIFT_CERTIFICATE, giftCertificateService.getCertificateById(id)));
    }

    @GetMapping("/search/by-tag")
    public ResponseEntity<?> getCertificatesByTagName(@RequestParam("name") String name, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getGiftCertificatesByTagName(name, page, size)), HttpStatus.OK);
    }

    @GetMapping("/search/by-part-name-description")
    public ResponseEntity<?> getCertificatesByPartOfNameOrDescription(@RequestParam("part") String nameOrDescriptionPart, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getGiftCertificatesByPart(nameOrDescriptionPart, page, size)), HttpStatus.OK);
    }


    @GetMapping("/sort")
    public ResponseEntity<?> getSortedCertificates(
            @Parameter(description = "Sorting criteria, e.g. -name,date. The '-' prefix indicates descending order. " +
                    "The default order is ascending. The first sorting criterion is name. Example: sort=-name,date")
            @RequestParam("sort") String sortString, @RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page, @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        String[] sort = sortString.split(",");

        Pattern patternName = Pattern.compile(NAME_REGEX);
        Pattern patternDate = Pattern.compile(DATE_REGEX);

        if (sort.length == 1) {
            String param = sort[0];
            DirectionEnum directionEnum = DirectionEnum.getDirection(param);
            if (patternDate.matcher(param).find())
                return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByDate(directionEnum, page, size)), HttpStatus.OK);
            if (patternName.matcher(param).find())
                return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByName(directionEnum, page, size)), HttpStatus.OK);
            else throw new ObjectIsInvalidException("Wrong sort parameter provided!");
        } else if (sort.length == 2) {
            String name = sort[0];
            String date = sort[1];
            if (patternName.matcher(name).find() && patternDate.matcher(date).find()) {
                DirectionEnum nameDirection = DirectionEnum.getDirection(name);
                DirectionEnum dateDirection = DirectionEnum.getDirection(date);
                return new ResponseEntity<>(Map.of(OBJECTS, giftCertificateService.getCertificatesSortedByDateName(nameDirection, dateDirection, page, size)), HttpStatus.OK);
            } else throw new ObjectIsInvalidException("Wrong sort parameters provided!");
        }
        throw new ObjectIsInvalidException("Please, provide correct number of sort parameters sort parameters");
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

