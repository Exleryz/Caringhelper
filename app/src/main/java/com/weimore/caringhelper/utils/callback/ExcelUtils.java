package com.weimore.caringhelper.utils.callback;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.weimore.base.BaseActivity;
import com.weimore.caringhelper.base.MyApplication;
import com.weimore.caringhelper.dao.helper.ContactBeanHelper;
import com.weimore.caringhelper.entity.Contact;
import com.weimore.net.BaseCallback;
import com.weimore.util.L;
import com.weimore.util.PermissionUtil;
import com.weimore.util.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import static android.app.Activity.RESULT_OK;

/**
 * @author Weimore
 *         2019/1/4.
 *         description:
 */

public class ExcelUtils {

    public static final int CHOOSE_FILE = 1001;

    public static void fileChoose(Activity context) {
        PermissionUtil.permissionRequest(context, () -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/*");//设置类型
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            context.startActivityForResult(intent, CHOOSE_FILE);

        }, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    public static void onActivityResult(Context context, int requestCode, int resultCode, Intent data,MyCallback<Boolean> callback) {
        if (resultCode == RESULT_OK && requestCode == CHOOSE_FILE && data != null) {
            L.e("选择的文件Uri = " + data.toString());
            final String excelPath = getRealFilePath(context, data.getData());
            L.e("excelPath = " + excelPath);
            if (excelPath.contains(".xls")) {
                if(excelPath.contains(".xlsx")){
                    ToastUtil.showShort("不支持该格式，目前只支持.xls格式的文件");
                    return;
                }
                //载入excel
                if(context!=null && context instanceof BaseActivity){
                    ((BaseActivity) context).showLoading();
                }
                boolean result = readExcel(excelPath);
                callback.callback(result);
                if(context!=null && context instanceof BaseActivity){
                    ((BaseActivity) context).dismissLoading();
                }
            } else {
                ToastUtil.showShort("此文件不是excel格式");
            }
        }


    }

    private static boolean readExcel(String excelPath) {
        try {
            File fis = new File(excelPath);
            Workbook book = Workbook.getWorkbook(fis);
            Sheet sheet = book.getSheet(0);
            for (int j = 0; j < sheet.getRows(); ++j) {
                Contact contact = new Contact(sheet.getCell(0, j).getContents(), sheet.getCell(1, j).getContents());
                ContactBeanHelper.insertData(contact);
            }
            book.close();
            return true;
        } catch (IOException | BiffException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据Uri获取真实图片路径
     * <p/>
     * 一个android文件的Uri地址一般如下：
     * content://media/external/images/media/62026
     *
     * @param context
     * @param uri
     * @return
     */
    private static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
