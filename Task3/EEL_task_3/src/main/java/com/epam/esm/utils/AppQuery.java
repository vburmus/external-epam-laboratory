package com.epam.esm.utils;

public class AppQuery {
    public static class Tag{
        public static final String CREATE_TAG = "INSERT INTO tag (name) VALUES(?)";
        public static final String DELETE_TAG = "DELETE  FROM tag WHERE id=?";
        public static final String GET_ALL_TAGS = "SELECT * FROM tag";
        public static final String GET_TAG_BY_ID = "SELECT * FROM tag WHERE id = ?";
        public static final String IS_TAG_EXISTS = "SELECT EXISTS(SELECT * FROM tag WHERE name=?)";
        public static final String GET_TAGS_ID = "SELECT id FROM tag WHERE name = ?";


    }
    public static class GiftCertificate{
        public static final String CREATE_CERTIFICATE = "INSERT INTO gift_certificate(name,description,price,duration,create_date,last_update_date) VALUES(?,?,?,?,?,?)";
        public static final String GET_ALL_CERTIFICATES = "SELECT * FROM certificate";
        public static final String GET_CERTIFICATE_BY_ID = "SELECT * FROM certificate WHERE id=?";
        public static final String DELETE_CERTIFICATE_BY_ID = "DELETE FROM gift_certificate WHERE id=?";
        public static final String GET_CERTIFICATES_ID = "SELECT id FROM gift_certificate WHERE name = ? AND description = ? AND price = ? AND duration = ?";
        public static final String IS_CERTIFICATE_EXISTS = "SELECT count(*) FROM certificate WHERE name = ? AND description = ? AND price = ? AND duration = ?";
    }

}
