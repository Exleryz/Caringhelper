package com.weimore.caringhelper.utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.weimore.caringhelper.entity.Contact;
import com.weimore.util.L;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Weimore
 *         2019/1/3.
 *         description:
 */

public class ContactUtils {

    public static List<Contact> getAllContactsFromPhone(Context context) {
        List<Contact> contactModelList = new ArrayList<>();
        //得到ContentResolver对象
        try {
            ContentResolver cr = context.getContentResolver();
            String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    cols, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                    int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String name = cursor.getString(nameFieldColumnIndex);
                    for (int m = 1; m <= numberFieldColumnIndex; m++) {
                        String number = cursor.getString(m).trim();
                        Contact model = new Contact(name,number);
                        Log.e("test", "name:" + name + "number:" + number);
                        contactModelList.add(model);
                    }
                }
                cursor.close();
                L.e("getAllContactsFromPhone:" + contactModelList.size());
                return contactModelList;
            } else {
                return new ArrayList<>();
            }
        }catch (Exception e){
            L.e(e.getMessage());
            return new ArrayList<>();
        }
    }

}
