package com.epam.esm.utils.datavalidation;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.exceptionhandler.exceptions.PageException;
import com.epam.esm.giftcertificate.direction.DirectionEnum;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.order.model.Order;
import com.epam.esm.tag.model.Tag;

import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ParamsValidation {
    private ParamsValidation() {}

    public static boolean isTagValid(Tag tag) {
        return tag != null && tag.getName() != null && !tag.getName().isEmpty();
    }

    public static boolean isCertificateHaveValidTags(List<Tag> tags) {
        if (tags != null) {
            for (Tag tag : tags) {
                if (!isTagValid(tag)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValidCertificate(GiftCertificate giftCertificate) {
        return giftCertificate.getName() != null && !giftCertificate.getName().isEmpty() && giftCertificate.getDuration() != null && giftCertificate.getDuration() >= 0 && giftCertificate.getDescription() != null && !giftCertificate.getDescription().isEmpty() && giftCertificate.getPrice() != null && giftCertificate.getPrice() >= 0 && isCertificateHaveValidTags(giftCertificate.getTags());
    }

    public static Optional<Map<String, String>> isPatchCertificateValid(@NonNull GiftCertificate giftCertificate) {

        Map<String, String> map = new HashMap<>();

        if (giftCertificate.getName() != null) map.put("name", giftCertificate.getName());

        if (giftCertificate.getDescription() != null) map.put("description", giftCertificate.getDescription());

        if (giftCertificate.getPrice() != null && giftCertificate.getPrice() >= 0)
            map.put("price", String.valueOf(giftCertificate.getPrice()));

        if (giftCertificate.getDuration() != null && giftCertificate.getDuration() >= 0)
            map.put("duration", String.valueOf(giftCertificate.getDuration()));

        if (map.isEmpty())
            throw new ObjectIsInvalidException("Please check params for certificate name = " + giftCertificate.getName());

        return Optional.of(map);
    }

    public static List<GiftCertificate> isCertificatesArentEmptyOrElseThrowNoSuchItem(List<GiftCertificate> giftCertificates, String message) {
        if (!giftCertificates.isEmpty()) return giftCertificates;
        throw new NoSuchItemException(message);
    }

    public static boolean isDirectionValid(String direction) {
        return direction.equals(DirectionEnum.ASC.name()) || direction.equals(DirectionEnum.DESC.name());
    }

    public static boolean isValidOrder(Order order) {
        return order.getUser().getId() != null && !order.getCertificates().isEmpty();
    }

    public static <T> List<T> isEmptyOrElseThrowPageException(List<T> list) {
        if(list.isEmpty())
            throw new PageException("Page must contain some data!");
        return list;
    }

}
