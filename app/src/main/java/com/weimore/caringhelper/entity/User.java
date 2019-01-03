package com.weimore.caringhelper.entity;

import lombok.Data;

/**
 * @author Weimore
 *         2018/12/29.
 *         description:
 */
@Data
public class User {

    /**
     * 用户Id
     */
    private String id;
    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户手机号 登录使用账号
     */
    private String phoneNo;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 用户注册时间
     */
    private String gmtCreate;

}
