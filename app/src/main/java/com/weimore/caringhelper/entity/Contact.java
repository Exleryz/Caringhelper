package com.weimore.caringhelper.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Weimore
 *         2019/1/3.
 *         description:
 */
@Data
@Entity
public class Contact {

    @Id(autoincrement = true)
    Long id;
    String name;
    @Unique
    @Property(nameInDb = "phone_num")
    String phoneNo;

    public Contact(String name,String phoneNo) {
        this.name = name;
        this.phoneNo = phoneNo;
    }

    @Generated(hash = 672515148)
    public Contact() {
    }

    @Generated(hash = 549330368)
    public Contact(Long id, String name, String phoneNo) {
        this.id = id;
        this.name = name;
        this.phoneNo = phoneNo;
    }

   

    public String getPhoneNo() {
        return this.phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
