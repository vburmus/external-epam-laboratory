package com.epam.esm.giftcerteficate.controller;


import com.epam.esm.giftcerteficate.model.GiftCertificate;
import com.epam.esm.giftcerteficate.repository.GiftCertificateRepository;
import com.epam.esm.giftcerteficate.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.List;

@Controller
@RequestMapping("/certificate")
public class GiftCertificateController {
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }
    @GetMapping()
    public String showAll(){;
        return "certificate/main";
    }
    @GetMapping("/new")
    public String newGiftCertificate(@ModelAttribute("certificate") GiftCertificate giftCertificate){
        return "certificate/new";
    }
    @PostMapping()
    public String createCertificate(@ModelAttribute("certificate") GiftCertificate giftCertificate) throws Exception {
            giftCertificateService.createCertificate(giftCertificate);
            return "redirect:/certificate";

        }

    }

