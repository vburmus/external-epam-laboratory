package com.epam.esm.utils.datavalidation;

import com.epam.esm.exceptionhandler.exceptions.rest.InvalidFileException;
import com.epam.esm.exceptionhandler.exceptions.rest.NoSuchItemException;
import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.tag.model.Tag;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.epam.esm.utils.Constants.*;

@UtilityClass
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
        return giftCertificate.getName() != null && !giftCertificate.getName().isEmpty() && giftCertificate.getDurationDate() != null && (LocalDateTime.now()).isBefore(giftCertificate.getDurationDate()) && giftCertificate.getDescription() != null && !giftCertificate.getDescription().isEmpty() && giftCertificate.getPrice() != null && giftCertificate.getPrice().compareTo(BigDecimal.ZERO) > 0 && isValidTags(giftCertificate.getTags());
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

    public static String validateFileExtension(MultipartFile file) {
        List<String> allowedImageExtension = List.of("jpg", "jpeg", "png", "gif");
        String fileName = file.getOriginalFilename();
        if (fileName == null) throw new InvalidFileException("Invalid file name");
        int index = fileName.lastIndexOf('.');
        String extension = "";
        if (index > 0) {
            extension = fileName.substring(index + 1);
        }
        if (extension.isEmpty() || !allowedImageExtension.contains(extension)) {
            throw new InvalidFileException("Invalid file extension");
        }
        return extension;
    }
}