package com.weimore.caringhelper.dao.helper;

import android.content.Context;

import com.weimore.caringhelper.base.MyApplication;
import com.weimore.caringhelper.dao.DatabaseManager;
import com.weimore.caringhelper.entity.Contact;
import com.weimore.caringhelper.greendao.gen.ContactDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author Weimore
 *         2018/11/22.
 *         description:
 */

public class ContactBeanHelper {


    private static ContactDao getDao() {
        return DatabaseManager.getDaoSession().getContactDao();
    }

    private Context getContext(){
        return MyApplication.Companion.getContext();
    }

    /**
     * 添加数据至数据库
     */
    public static void insertData(Contact bean) {
        Contact oldContact = queryOneByPhone(bean.getPhoneNo());
        if (oldContact != null) {
            oldContact.setName(bean.getName());
            updateData(oldContact);
        } else {
            getDao().insert(bean);
        }
    }


    /**
     * 将数据实体通过事务添加至数据库
     *
     * @param list
     */
    public static void insertData(List<Contact> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (Contact contact : list) {
            insertData(contact);
        }
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param bean
     */
    public static void saveData(Contact bean) {
        getDao().save(bean);
    }

    /**
     * 删除数据至数据库
     *
     * @param bean    删除具体内容
     */
    public static void deleteData(Contact bean) {
        getDao().delete(bean);
    }


    /**
     * 删除全部数据
     *
     */
    public static void deleteAllData() {
        getDao().deleteAll();
    }

    /**
     * 更新数据库
     *
     * @param bean
     */
    public static void updateData(Contact bean) {
        getDao().update(bean);
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public static List<Contact> queryAll() {
        QueryBuilder<Contact> builder = getDao().queryBuilder();
        return builder.build().list();
    }

    public static Contact queryOneByPhone(String phone) {
        QueryBuilder<Contact> builder = getDao().queryBuilder();
        builder.where(ContactDao.Properties.PhoneNo.eq(phone));
        return builder.unique();
    }

    public static boolean isContainPhone(String phone) {
        Contact contact = queryOneByPhone(phone);
        return contact != null;
    }

    public static Contact queryOneByName(String name) {
        QueryBuilder<Contact> builder = getDao().queryBuilder();
        builder.where(ContactDao.Properties.Name.eq(name));
        return builder.unique();
    }

    /**
     * 分页加载
     *
     * @param pageSize 当前第几页(程序中动态修改pageSize的值即可)
     * @param pageNum  每页显示多少个
     * @return
     */
    public static List<Contact> queryPaging(int pageSize, int pageNum) {
        List<Contact> listMsg = getDao().queryBuilder()
                .offset(pageSize * pageNum).limit(pageNum).list();
        return listMsg;
    }


}
