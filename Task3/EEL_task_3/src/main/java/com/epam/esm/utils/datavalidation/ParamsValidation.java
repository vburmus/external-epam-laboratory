package com.epam.esm.utils.datavalidation;

import com.epam.esm.exceptionhandler.exceptions.NoSuchItemException;
import com.epam.esm.exceptionhandler.exceptions.ObjectIsInvalidException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ParamsValidation {
    private ParamsValidation() {
    }

    public static boolean isTagValid(Tag tag) {
        return tag != null && tag.getName() != null && !tag.getName().isEmpty();
    }

    public static boolean isValidTags(List<Tag> tags) {
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
        return giftCertificate.getName() != null && !giftCertificate.getName().isEmpty() && giftCertificate.getDuration() != null && giftCertificate.getDuration() >= 0 && giftCertificate.getDescription() != null && !giftCertificate.getDescription().isEmpty() && giftCertificate.getPrice() != null && giftCertificate.getPrice().compareTo(BigDecimal.ZERO) > 0 && isValidTags(giftCertificate.getTags());
    }

    public static Map<String, String> isPatchCertificateValid(@NonNull GiftCertificate giftCertificate) {

        if (!ParamsValidation.isValidTags(giftCertificate.getTags()) && !giftCertificate.getTags().isEmpty())
            throw new ObjectIsInvalidException("Tags in gc are invalid");

        Map<String, String> map = new HashMap<>();

        if (giftCertificate.getName() != null) map.put("name", giftCertificate.getName());

        if (giftCertificate.getDescription() != null) map.put("description", giftCertificate.getDescription());

        if (giftCertificate.getPrice() != null && giftCertificate.getPrice().compareTo(BigDecimal.ZERO) > 0)
            map.put("price", String.valueOf(giftCertificate.getPrice()));

        if (giftCertificate.getDuration() != null && giftCertificate.getDuration() >= 0)
            map.put("duration", String.valueOf(giftCertificate.getDuration()));
        if (map.isEmpty())
            throw new ObjectIsInvalidException("Please check params for certificate name = " + giftCertificate.getName());

        return map;
    }

    public static <T> Page<T> isListIsNotEmptyOrElseThrowNoSuchItem(Page<T> objects) {
        if (!objects.isEmpty()) return objects;
        throw new NoSuchItemException("List is empty!");
    }

    public static boolean ifCertificateNeedsAnUpdate(GiftCertificate updateGc, GiftCertificate gcFromDB) {
        if (updateGc.getName() != null && !updateGc.getName().equals(gcFromDB.getName()))
            return true;
        if (updateGc.getDescription() != null && !updateGc.getDescription().equals(gcFromDB.getDescription()))
            return true;
        if (updateGc.getPrice() != null && updateGc.getPrice().compareTo(gcFromDB.getPrice()) > 0)
            return true;
        if (updateGc.getDuration() != null && updateGc.getDuration().equals(gcFromDB.getDuration()))
            return true;
        return updateGc.getTags() == null || updateGc.getTags() != gcFromDB.getTags();
    }

    public static Sort.Direction getSortDirection(String s) {
        return s.contains("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    public static String getSortParam(String s) {
        if (s.contains("date"))
            return "createDate";
        return s.contains("-") ? s.substring(1) : s;
    }

    public static boolean isPartValidForSearch(String part) {
        return part != null && !part.isEmpty();
    }


}
