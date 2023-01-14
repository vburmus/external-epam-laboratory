package com.epam.esm.utils;

public class AppQuery {
    public static class Tag{
        public static final String CREATE_TAG = "INSERT INTO tag (name) VALUES(?)";
        public static final String DELETE_TAG = "DELETE  FROM tag WHERE id=?";
        public static final String GET_ALL_TAGS = "SELECT * FROM tag";
        public static final String GET_TAG_BY_ID = "SELECT * FROM tag WHERE id = ?";
        public static final String GET_TAG_BY_NAME = "SELECT * FROM tag WHERE name = ?";
        public static final String IS_TAG_EXISTS = "SELECT count(*) FROM tag WHERE name=?";
        public static final String GET_TAGS_ID = "SELECT id FROM tag WHERE name = ?";


    }
    public static class GiftCertificate{
        public static final String CREATE_CERTIFICATE = "INSERT INTO gift_certificate(name,description,price,duration,create_date,last_update_date) VALUES(?,?,?,?,?,?)";
        public static final String GET_ALL_CERTIFICATES = "SELECT * FROM gift_certificate";
        public static final String GET_CERTIFICATE_BY_ID = "SELECT * FROM gift_certificate WHERE id=?";
        public static final String DELETE_CERTIFICATE_BY_ID = "DELETE FROM gift_certificate WHERE id=?";
        public static final String GET_CERTIFICATES_ID = "SELECT id FROM gift_certificate WHERE name = ? AND description = ? AND price = ? AND duration = ?";
        public static final String IS_CERTIFICATE_EXISTS = "SELECT count(*) FROM gift_certificate WHERE name = ? AND description = ? AND price = ? AND duration = ?";
        public static final String UPDATE_CERTIFICATE = "UPDATE gift_certificate SET";
         }
    public static class GiftCertificateHasTag{
        public final static String ADD_CERTIFICATE_TO_TAG = "INSERT INTO gift_certificate_has_tag (gift_certificate_id,tag_id) VALUES (?,?)";
        public final static String DELETE_CERTIFICATE_FROM_TAG = "DELETE  FROM gift_certificate_has_tag WHERE gift_certificate_id = ? AND tag_id = ?";
        public static final String GET_ALL_TAGS_BY_CERTIFICATE_ID =  "SELECT tag.id,tag.name FROM tag,gift_certificate_has_tag where tag.id = gift_certificate_has_tag.tag_id and gift_certificate_id=?";

    }
}
