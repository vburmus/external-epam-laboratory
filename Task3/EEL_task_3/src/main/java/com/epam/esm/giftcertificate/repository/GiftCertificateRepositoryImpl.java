package com.epam.esm.giftcertificate.repository;

import com.epam.esm.giftcertificate.model.GiftCertificate;
import com.epam.esm.utils.AppQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createGiftCertificate(GiftCertificate giftCertificate) {
        LocalDateTime createTime = LocalDateTime.now();
        LocalDateTime updateTime = LocalDateTime.now();
        return jdbcTemplate.update(AppQuery.GiftCertificate.CREATE_CERTIFICATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                createTime,
                updateTime) == 1;
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates() {
        return jdbcTemplate.query(AppQuery.GiftCertificate.GET_ALL_CERTIFICATES, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public GiftCertificate getGiftCertificateByID(long id) {
        return jdbcTemplate.query(AppQuery.GiftCertificate.GET_CERTIFICATE_BY_ID, new Object[]{id}, new BeanPropertyRowMapper<>(GiftCertificate.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public boolean deleteGiftCertificate(long id) {
        return jdbcTemplate.update(AppQuery.GiftCertificate.DELETE_CERTIFICATE_BY_ID, id) == 1;
    }

    @Override
    public boolean isGiftCertificateExist(GiftCertificate giftCertificate) {
        return jdbcTemplate.queryForObject(AppQuery.GiftCertificate.IS_CERTIFICATE_EXISTS, Integer.class,
                new Object[]{giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration()}) == 1;

    }

    @Override
    public long getGiftCertificatesID(GiftCertificate giftCertificate) {
        return jdbcTemplate.queryForObject(AppQuery.GiftCertificate.GET_CERTIFICATES_ID, Long.class, new Object[]{giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration()});
    }

    @Override
    public boolean updateGiftCertificate(long id, Map<String, String> updatedGiftCertificate) {
        StringBuilder updateQuery = new StringBuilder(AppQuery.GiftCertificate.UPDATE_CERTIFICATE);
        List<String> updateValues = new ArrayList<>();
        updatedGiftCertificate.forEach((key,value) -> {
            updateQuery.append(" ").append(key).append("= ?,");
            updateValues.add(value);
        });
        updateQuery.append("last_update_date = ? WHERE id = ?");
        updateValues.add(String.valueOf(LocalDateTime.now()));
        updateValues.add(String.valueOf(id));
        return jdbcTemplate.update(updateQuery.toString(), updateValues.toArray()) == 1;
    }


    public void addTagDependency(long giftcertificateID, long tagID) {
        jdbcTemplate.update(AppQuery.GiftCertificateHasTag.ADD_CERTIFICATE_TO_TAG, giftcertificateID, tagID);
    }

    public void deleteTagDependency(long giftcertificateID, long tagID) {
        jdbcTemplate.update(AppQuery.GiftCertificateHasTag.DELETE_CERTIFICATE_FROM_TAG, giftcertificateID, tagID);
    }

    @Override
    public void deleteTagDependenciesForGiftCertificate(List<Long> tags, long giftCertificateID) {
        tags.forEach(tagID -> deleteTagDependency(giftCertificateID, tagID));

    }

    @Override
    public void createTagDependenciesForGiftCertificate(List<Long> tags, long giftCertificateID) {
        tags.forEach(tagID -> addTagDependency(giftCertificateID, tagID));
    }
}
