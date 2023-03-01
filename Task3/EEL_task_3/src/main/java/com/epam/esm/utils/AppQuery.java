package com.epam.esm.utils;

import com.epam.esm.giftcertificate.direction.DirectionEnum;
import com.epam.esm.tag.model.Tag;
import java.util.List;


public class AppQuery {

    public static String getQueryWithPagination(String query,int page,int size){
        return query + " LIMIT " + size + " OFFSET " + (page-1)*size;
    }
    public static class Tag {
        public static final String CREATE_TAG = "INSERT INTO tag (name) VALUES(?)";
        public static final String DELETE_TAG = "DELETE  FROM tag WHERE id=?";
        public static final String GET_ALL_TAGS = "SELECT * FROM tag";
        public static final String GET_TAG_BY_ID = "SELECT * FROM tag WHERE id = ?";
        public static final String GET_TAG_BY_NAME = "SELECT * FROM tag WHERE name = ?";
        public static final String IS_TAG_EXISTS = "SELECT count(*) FROM tag WHERE name=?";
        public static final String GET_TAGS_ID = "SELECT id FROM tag WHERE name = ?";
        public static final String GET_MOST_USED_TAG = "SELECT tag.id,tag.name FROM (SELECT user_id, tag_id, COUNT(*) AS tag_count FROM lab_task.gift_certificate_has_tag " +
                "  JOIN lab_task.gift_certificate_has_order ON gift_certificate_has_order.gift_certificate_id = gift_certificate_has_tag.gift_certificate_id " +
                "  JOIN lab_task.purchase ON purchase.id = gift_certificate_has_order.order_id WHERE purchase.user_id = ( SELECT user_id FROM lab_task.purchase " +
                "  ORDER BY cost DESC LIMIT 1 ) GROUP BY user_id, tag_id ORDER BY tag_count DESC LIMIT 1) subquery JOIN lab_task.tag ON tag.id = subquery.tag_id;";

    }

    public static class GiftCertificate {
        public static final String CREATE_CERTIFICATE = "INSERT INTO gift_certificate(name,description,price,duration,create_date,last_update_date) VALUES(?,?,?,?,?,?)";
        public static final String GET_ALL_CERTIFICATES = "SELECT * FROM gift_certificate";
        public static final String GET_CERTIFICATES_PRICE = "SELECT price FROM gift_certificate WHERE id =?";
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
        public static  String getQueryForGettingSeveralTags(List<Long> tags) {
            StringBuilder sb = new StringBuilder("SELECT gc.id FROM gift_certificate gc");
            String part1 = " JOIN gift_certificate_has_tag gct";
            String part2 = " ON gc.id = gct";
            String part3 = ".gift_certificate_id AND gct";
            String part4 = ".tag_id = ";
            String part5 = " GROUP BY gc.id";

            for(int i = 0; i < tags.size();i++){
                sb.append(part1 + i + part2 + i + part3 + i + part4 + tags.get(i));
            }
            return sb.append(part5+ " ").toString();

        }
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
        public static final String GET_ALL_ORDERS= "SELECT * FROM purchase";
        public static final String GET_ORDERS_BY_USER_ID = "SELECT * FROM purchase WHERE users_id = ?";
        public static final String GET_ORDER_COST_AND_TIMESTAMP_BY_ID = "SELECT cost,create_date FROM purchase WHERE id = ?";
        public static final String IS_ORDER_EXIST = "SELECT count(*) FROM purchase WHERE user_id = ? AND description = ? AND cost = ?";
        public static final String CREATE_ORDER = "INSERT INTO purchase(user_id,cost,description,create_date,last_update_date) VALUES(?,?,?,?,?)";
        public static final String ADD_GC_INTO_ORDER = "INSERT INTO gift_certificate_has_order(gift_certificate_id, order_id) VALUES(?,?)";
        public static final String GET_ORDERS_ID = "SELECT id FROM purchase WHERE user_id = ? AND description = ? AND cost = ?";
        public static final String IS_GC_IN_ORDER_EXIST = "SELECT count(*) FROM gift_certificate_has_order WHERE order_id = ? AND gift_certificate_id = ? ";
        public static final String INCREMENT_QUANTITY = "UPDATE gift_certificate_has_order SET quantity = quantity + 1 WHERE gift_certificate_id = ? AND order_id = ?  ";
    }
}
