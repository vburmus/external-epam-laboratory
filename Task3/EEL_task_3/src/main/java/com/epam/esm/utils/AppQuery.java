package com.epam.esm.utils;

import com.epam.esm.taggiftcertificate.direction.DirectionEnum;


public class AppQuery {
    public static class Tag {
        public static final String CREATE_TAG = "INSERT INTO tag (name) VALUES(?)";
        public static final String DELETE_TAG = "DELETE  FROM tag WHERE id=?";
        public static final String GET_ALL_TAGS = "SELECT * FROM tag";
        public static final String GET_TAG_BY_ID = "SELECT * FROM tag WHERE id = ?";
        public static final String GET_TAG_BY_NAME = "SELECT * FROM tag WHERE name = ?";
        public static final String IS_TAG_EXISTS = "SELECT count(*) FROM tag WHERE name=?";
        public static final String GET_TAGS_ID = "SELECT id FROM tag WHERE name = ?";


    }

    public static class GiftCertificate {
        public static final String CREATE_CERTIFICATE = "INSERT INTO gift_certificate(name,description,price,duration,create_date,last_update_date) VALUES(?,?,?,?,?,?)";
        public static final String GET_ALL_CERTIFICATES = "SELECT * FROM gift_certificate";
        public static final String GET_CERTIFICATE_BY_ID = "SELECT * FROM gift_certificate WHERE id=?";
        public static final String DELETE_CERTIFICATE_BY_ID = "DELETE FROM gift_certificate WHERE id=?";
        public static final String GET_CERTIFICATES_ID = "SELECT id FROM gift_certificate WHERE name = ? AND description = ? AND price = ? AND duration = ?";
        public static final String IS_CERTIFICATE_EXISTS = "SELECT count(*) FROM gift_certificate WHERE name = ? AND description = ? AND price = ? AND duration = ?";
        public static final String UPDATE_CERTIFICATE = "UPDATE gift_certificate SET";
    }


    public static class GiftCertificateHasTag {
        public static final String ADD_CERTIFICATE_TO_TAG = "INSERT INTO gift_certificate_has_tag (gift_certificate_id,tag_id) VALUES (?,?)";
        public static final String DELETE_CERTIFICATE_FROM_TAG = "DELETE  FROM gift_certificate_has_tag WHERE gift_certificate_id = ? AND tag_id = ?";
        public static final String GET_ALL_TAGS_BY_CERTIFICATE_ID = "SELECT t.id,t.name FROM  gift_certificate_has_tag cert_tag JOIN tag t WHERE t.id = cert_tag.tag_id AND cert_tag.gift_certificate_id = ?";
        public static final String GET_GIFT_CERTIFICATE_BY_TAGS_NAME = "SELECT  cert.id , cert.name , cert.description, cert.price, cert.duration ,cert.create_date , cert.last_update_date FROM gift_certificate_has_tag cert_tag JOIN tag t JOIN gift_certificate cert  WHERE t.id = cert_tag.tag_id AND cert.id = cert_tag.gift_certificate_id  AND t.name = ?";
        public static final String GET_GIFT_CERTIFICATE_BY_PART_OF_NAME = "SELECT  * FROM  gift_certificate  WHERE name LIKE ?";
        public static final String GET_GIFT_CERTIFICATE_BY_PART_OF_DESCRIPTION = "SELECT  * FROM  gift_certificate  WHERE description LIKE ?";

        public static String getSortingQueryForOneParam(DirectionEnum direction, String param) {
            return "SELECT * FROM gift_certificate  ORDER BY " + param + " " + direction;
        }

        public static String getSortingQueryForTwoParams(DirectionEnum direction1, String param1, DirectionEnum direction2, String param2) {
             return "SELECT * FROM gift_certificate ORDER BY " + param1 + " " + direction1 + " , " + param2 + " " +direction2;
        }

    }
    public static class User {
        public static final String GET_ALL_USERS = "SELECT * FROM user";
        public static final String GET_USER_BY_ID = "SELECT * FROM user WHERE id = ?";

    }
    public static class Order {
        public static final String GET_ALL_ORDERS= "SELECT * FROM order";
        public static final String GET_ORDERS_BY_USER_ID = "SELECT * FROM order WHERE users_id = ?";
        public static final String GET_ORDER_COST_AND_TIMESTAMP_BY_ID = "SELECT cost,create_date FROM order WHERE id = ?";
    }
}
