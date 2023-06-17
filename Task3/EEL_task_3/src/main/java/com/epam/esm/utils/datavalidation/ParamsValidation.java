package com.epam.esm.utils.datavalidation;

import com.epam.esm.exceptionhandler.exceptions.rest.NoSuchItemException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

import static com.epam.esm.utils.Constants.*;


public class ParamsValidation {

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


    public static <T> Page<T> isListIsNotEmptyOrElseThrowNoSuchItem(Page<T> objects) {
        if (!objects.isEmpty()) return objects;
        throw new NoSuchItemException(LIST_IS_EMPTY);
    }

    public static Sort.Direction getSortDirection(String s) {
        return s.contains("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    public static String getSortParam(String s) {
        if (s.contains(DATE))
            return CREATE_DATE;
        return s.contains("-") ? s.substring(1) : s;
    }

    public static boolean isPartValidForSearch(String part) {
        return part != null && !part.isEmpty();
    }


}
