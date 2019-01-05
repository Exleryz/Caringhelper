package com.weimore.caringhelper.config;

public class ConstantUrl {

    /**
     * 后台接口调用网址
     */
    public static final String BASE_URL = "http://47.106.10.108:8080/";

    /**
     * 用户注册
     */
    public static final String REGISTER = BASE_URL + "user/register";

    /**
     * 用户登录
     */
    public static final String LOGIN = BASE_URL + "user/login";

    /**
     * 获取用户详情
     */
    public static final String GET_INFO = BASE_URL + "user/getUserInfo";

    /**
     * 更新用户信息
     */
    public static final String UPDATE_INFO = BASE_URL + "user/updateUserInfo";

    /**
     * 修改密码
     */
    public static final String UPDATE_PASSWORD = BASE_URL + "user/updatePwd";

    /**
     * 获取云端联系人
     */
    public static final String GET_ALL_CONTACTS = BASE_URL + "contact/getAllContacts";

    /**
     * 上传联系人到云端
     */
    public static final String UPLOAD_CONTACTS = BASE_URL + "contact/uploadWhiteContacts";

    /**
     * 添加/更新 联系人
     */
    public static final String ADD_CONTACT = BASE_URL + "contact/addWhiteContact";

    /**
     * 删除联系人
     */
    public static final String DELETE_CONTACT = BASE_URL + "contact/deleteContact";

    /**
     * 删除所有联系人
     */
    public static final String DELETE_ALL_CONTACTS = BASE_URL + "contact/deleteContacts";

}
