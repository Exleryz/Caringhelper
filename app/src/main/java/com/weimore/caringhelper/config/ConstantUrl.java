package com.weimore.caringhelper.config;

/**
 * @author Weimore
 *         2019/1/2.
 *         description:
 */

public class ConstantUrl {

    public static final String BASE_URL = "http://47.106.10.108:8080/";

    public static final String REGISTER = BASE_URL + "user/register";

    public static final String LOGIN = BASE_URL + "user/login";

    public static final String GET_INFO = BASE_URL + "user/getUserInfo";

    public static final String UPDATE_INFO = BASE_URL + "user/updateUserInfo";

    public static final String UPDATE_PASSWORD = BASE_URL + "user/updatePwd";


    public static final String GET_ALL_CONTACTS = BASE_URL + "contact/getAllContacts";

    public static final String UPLOAD_CONTACTS = BASE_URL + "contact/uploadWhiteContacts";

    public static final String ADD_CONTACT = BASE_URL  + "contact/addWhiteContact";

    public static final String DELETE_CONTACT = BASE_URL + "contact/deleteContact";

    public static final String DELETE_ALL_CONTACTS = BASE_URL + "contact/deleteContacts";

}
