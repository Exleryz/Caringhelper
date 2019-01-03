package com.weimore.caringhelper.dao.helper;

import android.content.Context;

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


    private static ContactDao getDao(Context context) {
        return DatabaseManager.getDaoSession().getContactDao();
    }

    /**
     * 添加数据至数据库
     * mE
     *
     * @param context
     */
    public static void insertData(Context context, Contact bean) {
        Contact oldContact = queryOneByPhone(context, bean.getPhoneNo());
        if (oldContact != null) {
            oldContact.setName(bean.getName());
            updateData(context, oldContact);
        } else {
            getDao(context).insert(bean);
        }
    }


    /**
     * 将数据实体通过事务添加至数据库
     *
     * @param context
     * @param list
     */
    public static void insertData(Context context, List<Contact> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (Contact contact : list) {
            insertData(context, contact);
        }
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；
     *
     * @param context
     * @param bean
     */
    public static void saveData(Context context, Contact bean) {
        getDao(context).save(bean);
    }

    /**
     * 删除数据至数据库
     *
     * @param context
     * @param bean    删除具体内容
     */
    public static void deleteData(Context context, Contact bean) {
        getDao(context).delete(bean);
    }

//    /**
//     * 根据id删除数据至数据库
//     *
//     * @param context
//     * @param id      删除具体内容
//     */
//    public static void deleteByKeyData(Context context, Long id) {
//        getDao(context).deleteByKey(id);
//    }

    /**
     * 删除全部数据
     *
     * @param context
     */
    public static void deleteAllData(Context context) {
        getDao(context).deleteAll();
    }

    /**
     * 更新数据库
     *
     * @param context
     * @param bean
     */
    public static void updateData(Context context, Contact bean) {
        getDao(context).update(bean);
    }

    /**
     * 查询所有数据
     *
     * @param context
     * @return
     */
    public static List<Contact> queryAll(Context context) {
        QueryBuilder<Contact> builder = getDao(context).queryBuilder();
        return builder.build().list();
    }

    public static Contact queryOneByPhone(Context context, String phone) {
        QueryBuilder<Contact> builder = getDao(context).queryBuilder();
        builder.where(ContactDao.Properties.PhoneNo.eq(phone));
        return builder.unique();
    }

    public static boolean isContainPhone(Context context, String phone) {
        Contact contact = queryOneByPhone(context, phone);
        return contact != null;
    }

    public static Contact queryOneByName(Context context, String name) {
        QueryBuilder<Contact> builder = getDao(context).queryBuilder();
        builder.where(ContactDao.Properties.Name.eq(name));
        return builder.unique();
    }

    /**
     * 分页加载
     *
     * @param context
     * @param pageSize 当前第几页(程序中动态修改pageSize的值即可)
     * @param pageNum  每页显示多少个
     * @return
     */
    public static List<Contact> queryPaging(int pageSize, int pageNum, Context context) {
        List<Contact> listMsg = getDao(context).queryBuilder()
                .offset(pageSize * pageNum).limit(pageNum).list();
        return listMsg;
    }


}
