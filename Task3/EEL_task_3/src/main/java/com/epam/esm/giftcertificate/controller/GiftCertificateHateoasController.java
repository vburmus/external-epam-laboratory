package com.epam.esm.giftcertificate.controller;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.giftcertificate.service.GiftCertificateService;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}")
    public EntityModel<GiftCertificate> getById(@PathVariable("id") long id) {
        GiftCertificate gc = giftCertificateService.getCertificateById(id);
        Link selfLink = linkTo(methodOn(GiftCertificateController.class).getById(id)).withSelfRel();
        gc.add(selfLink);
        return EntityModel.of(gc);
    }
}
