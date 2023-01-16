package com.epam.esm.taggiftcertificate.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.taggiftcertificate.direction.DirectionEnum;
import com.epam.esm.utils.AppQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagGiftCertificateRepositoryImpl implements TagGiftCertificateRepository {
    private static final String CREATE_DATE = "create_date";
    private static final String NAME = "name";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagGiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesByTagName(String tagName) {
        return jdbcTemplate.query(AppQuery.GiftCertificateHasTag.GET_GIFT_CERTIFICATE_BY_TAGS_NAME, new BeanPropertyRowMapper<>(GiftCertificate.class), tagName);
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesByPartOfName(String part) {
        return jdbcTemplate.query(AppQuery.GiftCertificateHasTag.GET_GIFT_CERTIFICATE_BY_PART_OF_NAME, new BeanPropertyRowMapper<>(GiftCertificate.class), part + "%");
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesByPartOfDescription(String part) {
        return jdbcTemplate.query(AppQuery.GiftCertificateHasTag.GET_GIFT_CERTIFICATE_BY_PART_OF_DESCRIPTION, new BeanPropertyRowMapper<>(GiftCertificate.class), part + "%");
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByDate(DirectionEnum direction) {
        return jdbcTemplate.query(AppQuery.GiftCertificateHasTag.getSortingQueryForOneParam(direction, CREATE_DATE), new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByName(DirectionEnum direction) {
        return jdbcTemplate.query(AppQuery.GiftCertificateHasTag.getSortingQueryForOneParam(direction, NAME), new BeanPropertyRowMapper<>(GiftCertificate.class));

    }

    @Override
    public List<GiftCertificate> getCertificatesSortedByDateName(DirectionEnum directionDate, DirectionEnum directionName) {
        return jdbcTemplate.query(AppQuery.GiftCertificateHasTag.getSortingQueryForTwoParams(directionDate, CREATE_DATE, directionName, NAME), new BeanPropertyRowMapper<>(GiftCertificate.class));

    }
}
