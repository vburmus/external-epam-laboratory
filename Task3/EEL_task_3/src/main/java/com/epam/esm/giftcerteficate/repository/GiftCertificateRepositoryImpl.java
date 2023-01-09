package com.epam.esm.giftcerteficate.repository;

import com.epam.esm.giftcerteficate.model.GiftCertificate;
import com.epam.esm.utils.AppQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository{
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
                //TODO duration
                giftCertificate.getDuration(),
                createTime,
                updateTime)==1;
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates() {
        return jdbcTemplate.query(AppQuery.GiftCertificate.GET_ALL_CERTIFICATES, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public GiftCertificate getGiftCertificateByID(long id) {
        return jdbcTemplate.query(AppQuery.GiftCertificate.GET_CERTIFICATE_BY_ID,new Object[]{id},new BeanPropertyRowMapper<>(GiftCertificate.class))
                .stream().findAny().orElse(null);//TODO exception
    }

    @Override
    public boolean deleteGiftCertificate(long id) {
        return jdbcTemplate.update(AppQuery.GiftCertificate.DELETE_CERTIFICATE_BY_ID, id)==1;
    }

    @Override
    public boolean isGiftCertificateExist(GiftCertificate giftCertificate) {
        GiftCertificate g = giftCertificate;
        return jdbcTemplate.queryForObject(AppQuery.GiftCertificate.IS_CERTIFICATE_EXISTS,Integer.class,
                new Object[]{giftCertificate.getName(),giftCertificate.getDescription(),giftCertificate.getPrice(),giftCertificate.getDuration()})==1;

    }

    @Override
    public long getGiftCertificatesID(GiftCertificate giftCertificate) {
        return jdbcTemplate.queryForObject(AppQuery.GiftCertificate.GET_CERTIFICATES_ID,Long.class,new Object[]{giftCertificate.getName(),giftCertificate.getDescription(),giftCertificate.getPrice(),giftCertificate.getDuration()});
    }

    @Override
    public boolean updateGiftCertificate(long id, GiftCertificate updatedGiftCertificate) {
        return jdbcTemplate.update(AppQuery.GiftCertificate.UPDATE_CERTIFICATE,updatedGiftCertificate.getName(),updatedGiftCertificate.getDescription(),updatedGiftCertificate.getPrice(),updatedGiftCertificate.getDuration(),LocalDateTime.now(),id)==1;
    }
//TODO
    @Override
    public boolean deleteTagDependencyForGiftCertificate(List<Long> tags, long giftCertificateID) {
        return false;
    }

    @Override
    public boolean createTagDependencyForGiftCertificate(List<Long> tags, long giftCertificateID) {
        return false;
    }
}
