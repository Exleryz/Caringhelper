package com.weimore.caringhelper.ui.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weimore.base.BasePresenter;
import com.weimore.caringhelper.base.MyApplication;
import com.weimore.caringhelper.config.ConfigKey;
import com.weimore.caringhelper.config.ConstantUrl;
import com.weimore.caringhelper.dao.helper.ContactBeanHelper;
import com.weimore.caringhelper.entity.Contact;
import com.weimore.caringhelper.entity.ContactsVO;
import com.weimore.caringhelper.net.OkHttpUtil;
import com.weimore.caringhelper.ui.contract.ContactContract;
import com.weimore.caringhelper.ui.model.ContactModel;
import com.weimore.caringhelper.utils.ContactUtils;
import com.weimore.net.BaseCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //先判断本地是否有数据，如果无数据，则从数据库拉取数据
        List<Contact> contactList = ContactBeanHelper.queryAll();
        if(contactList==null||contactList.size()==0){
            Map<String,Object> map = new HashMap<>();
            map.put("userId", ConfigKey.getUserId());
           OkHttpUtil.post(ConstantUrl.GET_ALL_CONTACTS, map, Contact[].class, new BaseCallback<List<Contact>>() {
               @Override
               public void callback(List<Contact> data) {
                   if(isNotDetach()){
                       getMView().dismissLoading();
                       //获取到服务器端数据，存入数据库
                       List<Contact> contacts = new Gson().fromJson(new Gson().toJson(data), new TypeToken<List<Contact>>() {}.getType());
                       if(contacts!=null && contacts.size()>0){
                           ContactBeanHelper.insertData(contacts);
                           getMView().setContactInfo(contacts);
                       }
                   }
               }

               @Override
               public void error(@org.jetbrains.annotations.Nullable String message) {
                   if(isNotDetach()){
                       getMView().dismissLoading();
                   }
               }
           });
        }else {
            //本地有数据，直接返回数据
            if(isNotDetach()){
                getMView().dismissLoading();
                getMView().setContactInfo(contactList);
            }
        }

    }

    @Override
    public void getContactFromPhone() {
        List<Contact> contactList = ContactUtils.getAllContactsFromPhone(MyApplication.Companion.getContext());
        if(isNotDetach()){
            getMView().dismissLoading();
            ContactBeanHelper.insertData(contactList);
            syncContactList();
            getMView().addPhoneContactInfoSuccess();
        }
    }

    @Override
    public void insertOrUpdateData(Contact contact) {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact);
        ContactsVO contactsVO = new ContactsVO(ConfigKey.getUserId(),contacts);
        OkHttpUtil.post(ConstantUrl.ADD_CONTACT,contactsVO,String.class,new BaseCallback<String>(){
            @Override
            public void callback(String data) {
                if(isNotDetach()){
                    getMView().dismissLoading();
                }
            }

            @Override
            public void error(@org.jetbrains.annotations.Nullable String message) {
                if(isNotDetach()){
                    getMView().dismissLoading();
                }
            }
        });
    }

    @Override
    public void deleteData(Contact contact) {
        List<Contact> contacts = new ArrayList<>();
        Contact newContact = new Contact(contact.getName(),contact.getPhoneNo());
        contacts.add(newContact);
        ContactsVO contactsVO = new ContactsVO(ConfigKey.getUserId(),contacts);
        OkHttpUtil.post(ConstantUrl.DELETE_CONTACT,contactsVO,String.class,new BaseCallback<String>(){
            @Override
            public void callback(String data) {
                if(isNotDetach()){
                    getMView().dismissLoading();
                }
            }

            @Override
            public void error(@org.jetbrains.annotations.Nullable String message) {
                if(isNotDetach()){
                    getMView().dismissLoading();
                }
            }
        });
    }

    @Override
    public void syncContactList() {
        List<Contact> contacts = ContactBeanHelper.queryAll();
        ContactsVO contactsVO = new ContactsVO(ConfigKey.getUserId(),contacts);
        OkHttpUtil.post(ConstantUrl.UPLOAD_CONTACTS,contactsVO,String.class,new BaseCallback<String>(){
            @Override
            public void callback(String data) {
                if(isNotDetach()){
                    getMView().dismissLoading();
                }
            }

            @Override
            public void error(@org.jetbrains.annotations.Nullable String message) {
                if(isNotDetach()){
                    getMView().dismissLoading();
                }
            }
        });

    }

}