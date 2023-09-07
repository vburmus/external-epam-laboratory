package com.epam.esm.giftcertificate.controller;

import com.epam.esm.giftcertificate.model.GiftCertificateDTO;
import com.epam.esm.giftcertificate.service.GiftCertificateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.util.List;

import static com.epam.esm.utils.Constants.*;

@RestController
@RequestMapping(value = "/certificate")
@Profile("default")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GiftCertificateDTO> createCertificate(@RequestPart("certificate") GiftCertificateDTO giftCertificateDTO,
                                                                @RequestParam @Nullable MultipartFile image) {
        return new ResponseEntity<>(giftCertificateService.createCertificate(giftCertificateDTO, image),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<GiftCertificateDTO>> showAll(@RequestParam(required = false, defaultValue =
            DEFAULT_PAGE) Integer page,
                                                            @RequestParam(required = false, defaultValue =
                                                                    DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(giftCertificateService.getAllGiftCertificates(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateDTO> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(giftCertificateService.getCertificateById(id));
    }

    @GetMapping("/search/by-tag")
    public ResponseEntity<Page<GiftCertificateDTO>> getCertificatesByTagName(@RequestParam("name") String name,
                                                                             @RequestParam(required = false,
                                                                                     defaultValue = DEFAULT_PAGE) Integer page,
                                                                             @RequestParam(required = false,
                                                                                     defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(giftCertificateService.getGiftCertificatesByTagName(name, page, size),
                HttpStatus.OK);
    }

    @GetMapping("/search/by-part-name-description")
    public ResponseEntity<Page<GiftCertificateDTO>> getCertificatesByPart(@RequestParam("part") String nameOrDescriptionPart,
                                                                          @RequestParam(required = false,
                                                                                  defaultValue = DEFAULT_PAGE) Integer page,
                                                                          @RequestParam(required = false,
                                                                                  defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(giftCertificateService.getGiftCertificatesByPart(nameOrDescriptionPart, page, size),
                HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity<Page<GiftCertificateDTO>> getSortedCertificates(@Parameter(description = SORT_DESCTIPTION) @RequestParam(
            "sort") String sortString,
                                                                          @RequestParam(required = false,
                                                                                  defaultValue = DEFAULT_PAGE) Integer page,
                                                                          @RequestParam(required = false,
                                                                                  defaultValue = DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(giftCertificateService.getCertificatesSortedByParam(sortString, page, size),
                HttpStatus.OK);
    }

    @GetMapping({"/search/by-tags"})
    public ResponseEntity<Page<GiftCertificateDTO>> searchForCertificatesBySeveralTags(@RequestParam("tagsId") List<Long> tagsId,
                                                                                       @RequestParam(required = false
                                                                                               , defaultValue =
                                                                                               DEFAULT_PAGE) Integer page,
                                                                                       @RequestParam(required = false
                                                                                               , defaultValue =
                                                                                               DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(giftCertificateService.getCertificatesBySeveralTags(tagsId, page, size),
                HttpStatus.OK);
    }

    @GetMapping({"/search/by-tags-and-part"})
    public ResponseEntity<Page<GiftCertificateDTO>> searchForCertificatesByTagsAndShortDescription(@RequestParam(
            "tagsId") List<Long> tagsId,
                                                                                        @RequestParam(
                                                                                                "part") String part,
                                                                                        @RequestParam(required = false
                                                                                                , defaultValue =
                                                                                                DEFAULT_PAGE) Integer page,
                                                                                        @RequestParam(required = false
                                                                                                , defaultValue =
                                                                                                DEFAULT_SIZE) Integer size) {
        return new ResponseEntity<>(giftCertificateService.getCertificatesByTagsAndShortDescriptionOrNamePart(tagsId,
                part,
                page, size),
                HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GiftCertificateDTO> updateCertificate(
            @PathVariable("id") long id,
            @RequestPart(value = "patch") JsonMergePatch patch,
            @RequestPart(value = "image", required = false) MultipartFile image) throws JsonPatchException, JsonProcessingException {
        return new ResponseEntity<>(giftCertificateService.updateCertificate(id, patch,image), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable("id") long id) {
        giftCertificateService.deleteCertificate(id);
        return ResponseEntity.ok().build();
    }
}