package com.epam.esm.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String DEFAULT_PAGE = "1";
    public static final String DEFAULT_SIZE = "3";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String DESCRIPTION = "description";
    public static final String DURATION = "duration";
    public static final String DURATION_DATE = "durationDate";
    public static final String DATE = "date";
    public static final String LAST_UPDATE_DATE = "lastUpdateDate";
    public static final String ID = "id";
    public static final String ROLE = "role";
    public static final String EPAM = "epam";
    public static final String TAGS = "tags";
    public static final String EMAIL = "email";
    public static final String PROVIDER = "provider";
    public static final String TYPE = "tags";
    public static final String PRICE = "price";
    public static final String SORT_DESCTIPTION =
            "Sorting criteria, e.g. -name,date. The " + "'-' prefix indicates descending order. " + "The default " +
                    "order is ascending. The first sorting criterion " + "is name. Example: sort=-name,date";

    public static final String LOCAL_PROVIDER = "LOCAL";

    public static final String ALREADY_REGISTERED = "Such user has already registered";
    public static final String USER_DOESNT_EXIST = "Such user doesnt exist";

    public static final String OTHER_INSTANCE_DETECTED = "Expected User instance, but other instance detected.";
    public static final String TAG_WITH_NAME = "Tag with name = ";
    public static final String ALREADY_EXISTS = " already exists";
    public static final String IS_INVALID = " is invalid";
    public static final String TAG_WITH_ID = "Tag with id = ";
    public static final String DOESN_T_EXIST = "doesn't exist";
    public static final String THERE_IS_NO_TAGS_IN_THIS_PURCHASE = "There is no tags in this purchase";

    public static final String LIST_IS_EMPTY = "List is empty!";

    public static final String NO_SUCH_ORDER = "No such order!";
    public static final String NO_USER_WITH_ID_D_FOUND = "No user with id = %d found";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHENTICATION_BEARER_TOKEN = "Bearer ";
    public static final String CACHE_NOT_FOUND = "Cache not found";


    public static final String SORT_REGEX = "^(-)?date$|^(-)?name$";

    public static final String TO_MANY_PARAMS_FOR_SORTING = "To many params for sorting";
    public static final String SOME_SORT_PARAMS_ARE_INVALID = "Some sort params are invalid.";
    public static final String THERE_IS_NO_GC_WITH_ID = "There is no gc with id = ";
    public static final String PART_OF_DESCRIPTION = "Part of description -> ";
    public static final String TAG_WITH_NAME1 = "Tag with name ";
    public static final String DOESN_T_EXIST1 = " doesn't exist";
    public static final String TAG_NAME = "Tag name ";
    public static final String GIFT_CERTIFICATE_WITH_ID = "GiftCertificate with id = ";
    public static final String GIFT_CERTIFICATE_WITH_NAME = "Gift certificate with name = ";
    public static final String DURATION1 = ", duration = ";

    public static final String IS_INVALID_PLEASE_CHECK_YOUR_PARAMS = " is invalid, please check your params";
    public static final String TOKEN_IS_INVALID = "Token is invalid";
    public static final String MISSING_USER_EMAIL = "Missing user email";

    public static final String ERROR_LOADING_KEYS = "An error occurred while loading keys.";
    public static final String KEYS_DON_T_EXIST = "Public and private keys don't exist.";
    public static final String DEFAULT_PROFILE = "default";
    public static final String ERROR_CREATING_KEYS = "An error occurred while creating keys.";

    public static final String ACCESS_TOKENS = "accessTokens";
    public static final String REFRESH_TOKENS = "refreshTokens";
    public static final String BLACK_LIST = "blackList";

    public static final String NOT_FOUND = "Not Found";
    public static final String ALREADY_EXIST = "Already Exist";
    public static final String INVALID_OBJECT = "Invalid Object";
    public static final String JSON_EXCEPTION = "Json Exception";
    public static final String FAILED_TO_CREATE_USER = "Failed to Create User";
    public static final String EMAIL_NOT_FOUND = "Email Not Found";
    public static final String INVALID_TOKEN = "Invalid Token";
    public static final String WRONG_AUTHENTICATION_INSTANCE = "Wrong Authentication Instance";

    public static final String GET_MOST_USED_TAG = "select tag.id,tag.name from (select user_id, tag_id, count(*) as tag_count from " +
            "gift_certificate_has_tag " +
            "  join gift_certificate_has_order on gift_certificate_has_order.gift_certificate_id = gift_certificate_has_tag" +
            ".gift_certificate_id " +
            "  join purchase on purchase.id = gift_certificate_has_order.order_id where purchase.user_id = ( select user_id from purchase" +
            "  order by cost desc limit 1 ) group by user_id, tag_id order by tag_count desc limit 1) subquery join tag on tag.id = " +
            "subquery.tag_id;";
    public static final String GET_GC_BY_TAGS_AND_PART = "SELECT id,name,description,price,duration_date," +
            "create_date,last_update_date FROM " +
            "gift_certificate AS gc LEFT JOIN gift_certificate_has_tag AS t ON gc.id= " +
            "t.gift_certificate_id WHERE tag_id IN :tags and " +
            "(description LIKE %:partial% OR name LIKE  %:partial%)";

    public static final String DEFAULT_PROFILE_IMG = "https://mjc-content.s3.eu-north-1.amazonaws.com/user/default-user.png";
    public static final String DEFAULT_TAG_IMG = "https://mjc-content.s3.eu-north-1.amazonaws.com/tag/default_tag.jpg";
    public static final String DEFAULT_CERTIFICATE_IMG = "https://mjc-content.s3.eu-north-1.amazonaws" +
            ".com/gift-certificate/default_certificate.jpg";

    public static final String FILE_HAD_WRONG_FORMAT = "File had wrong format";
    public static final String WRONG_EMAIL_OR_PASSWORD = "Wrong email or password";
}
