package com.epam.esm.giftcertificate.controller;

import com.epam.esm.giftcertificate.model.GiftCertificateDTO;
import com.epam.esm.giftcertificate.service.GiftCertificateService;
import com.epam.esm.utils.mappers.EntityToDtoMapper;
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
    private final EntityToDtoMapper entityToDtoMapper;

    public GiftCertificateHateoasController(GiftCertificateService giftCertificateService, EntityToDtoMapper entityToDtoMapper) {
        this.giftCertificateService = giftCertificateService;
        this.entityToDtoMapper = entityToDtoMapper;
    }

    @GetMapping("/{id}")
    public EntityModel<GiftCertificateDTO> getById(@PathVariable("id") long id) {
        GiftCertificateDTO gc = entityToDtoMapper.toGiftCertificateDTO(giftCertificateService.getCertificateById(id));
        Link selfLink = linkTo(methodOn(GiftCertificateController.class).getById(id)).withSelfRel();
        gc.add(selfLink);
        return EntityModel.of(gc);
    }
}
