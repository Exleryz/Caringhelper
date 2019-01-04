package com.weimore.caringhelper.entity;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Weimore
 *         2019/1/4.
 *         description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactsVO implements Serializable{

    private int userId;

    private List<Contact> contacts;

}
