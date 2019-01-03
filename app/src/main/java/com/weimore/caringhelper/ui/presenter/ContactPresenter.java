package com.weimore.caringhelper.ui.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weimore.base.BasePresenter;
import com.weimore.caringhelper.base.MyApplication;
import com.weimore.caringhelper.dao.helper.ContactBeanHelper;
import com.weimore.caringhelper.entity.Contact;
import com.weimore.caringhelper.greendao.gen.ContactDao;
import com.weimore.caringhelper.ui.contract.ContactContract;
import com.weimore.caringhelper.ui.model.ContactModel;
import com.weimore.caringhelper.utils.ContactUtils;

import java.util.List;

/**
 * @author Weimore
 *         2019/1/3.
 *         description:
 */

public class ContactPresenter extends BasePresenter<ContactContract.View, ContactContract.Model> implements ContactContract.Presenter {

    public ContactPresenter(@Nullable ContactContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    public ContactContract.Model getModel() {
        return new ContactModel();
    }

    @Override
    public void getContactInfo() {
        List<Contact> contactList = ContactBeanHelper.queryAll(MyApplication.Companion.getContext());
        if(isNotDetach()){
            getMView().dismissLoading();
            getMView().setContactInfo(contactList);
        }
    }

    @Override
    public void getContactFromPhone() {
        List<Contact> contactList = ContactUtils.getAllContactsFromPhone(MyApplication.Companion.getContext());
        if(isNotDetach()){
            getMView().dismissLoading();
            ContactBeanHelper.insertData(MyApplication.Companion.getContext(),contactList);
            getMView().addPhoneContactInfoSuccess();
        }
    }
}