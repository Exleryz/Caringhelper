package com.weimore.caringhelper.ui.contract;

import com.weimore.base.IBaseModel;
import com.weimore.base.IBasePresenter;
import com.weimore.base.IBaseView;
import com.weimore.caringhelper.entity.Contact;

import java.util.List;

/**
 * @author Weimore
 *         2019/1/3.
 *         description:
 */

public interface ContactContract {

    interface View extends IBaseView {

        void setContactInfo(List<Contact> contactList);

        void addPhoneContactInfoSuccess();
    }

    interface Presenter extends IBasePresenter {

        /**
         * 获取所有白名单数据
         */
        void getContactInfo();

        /**
         * 通讯录中用户导入白名单
         */
        void getContactFromPhone();
    }

    interface Model extends IBaseModel {


    }

}