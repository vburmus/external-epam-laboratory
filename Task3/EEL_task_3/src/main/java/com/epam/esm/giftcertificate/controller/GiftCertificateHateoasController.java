package com.epam.esm.giftcertificate.controller;

import com.epam.esm.giftcertificate.model.GiftCertificateHateoas;
import com.epam.esm.giftcertificate.service.GiftCertificateService;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.utils.Constants.DEFAULT_PAGE;
import static com.epam.esm.utils.Constants.DEFAULT_SIZE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/certificate")
@Profile("hateoas")
public class GiftCertificateHateoasController {

    private final GiftCertificateService giftCertificateService;


    public GiftCertificateHateoasController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping
    public CollectionModel<GiftCertificateHateoas> showAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                           @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size) {
        Page<GiftCertificateHateoas> certificatesPage =
                giftCertificateService.getAllGiftCertificates(--page, size).map(GiftCertificateHateoas::new);

        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(GiftCertificateHateoasController.class).showAll(page, size)).withSelfRel());
        if (page != 1)
            links.add(linkTo(methodOn(GiftCertificateHateoasController.class).showAll(page - 1, size)).withSelfRel());
        if (certificatesPage.hasNext())
            links.add(linkTo(methodOn(GiftCertificateHateoasController.class).showAll(page + 1, size)).withSelfRel());

        return CollectionModel.of(certificatesPage, links);
    }

    @GetMapping("/{id}")
    public RepresentationModel<?> getById(@PathVariable("id") long id) {
        return new GiftCertificateHateoas(giftCertificateService.getCertificateById(id));
    }
}
