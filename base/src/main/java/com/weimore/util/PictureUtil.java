package com.weimore.util;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.weimore.base.BaseApplication;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author Weimore
 *         2018/12/11.
 *         description:
 */

public class PictureUtil {

    public final static int ALBUM = 1001;
    public final static int CAMERA = 1002;
    public final static int CROP = 1003;

    public final static String CROP_PATH = File.separator + "CaringHelper" + File.separator + "crop";
    public final static String STORE_PATH = File.separator + "CaringHelper" + File.separator + "images";

    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");


    public static void startAlbum(Activity activity) {
        PermissionUtil.permissionRequest(activity, 200, () -> {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    activity.startActivityForResult(intent, ALBUM);
                }, android.Manifest.permission.READ_EXTERNAL_STORAGE
                , android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }



    /**
     * 跳转拍照
     */
    public static String startCamera(Activity activity) {
//        PermissionUtil.permissionRequest(activity, 200, () -> {
//                },android.Manifest.permission.CAMERA,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE
//                , android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        String filename = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            File directory = new File(activity.getFilesDir().getAbsolutePath() + STORE_PATH + File.separator);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            int randomNum = new Random().nextInt(100000) + 899999;
            filename = FORMAT.format(new Date()) + randomNum;
            File file = null;
            try {
                file = File.createTempFile(filename, ".jpg", directory);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //这里指定存储的uri后，拍照返回的data就会为空，所以要在该方法中照片存储的路径
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity, "com.weimore.base.fileProvider", file));
            activity.startActivityForResult(intent, CAMERA);
            return file.getAbsolutePath();
        } else {
            ToastUtil.showShort("没有存储卡，无法拍照，请求相册选择图片！");
            return "";
        }
    }

    /**
     * 裁剪图片方法实现
     */
    public static Uri startCrop(Activity activity, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        Uri uriCrop = getUri(CROP_PATH);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCrop);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, CROP);
        return uriCrop;
    }

    /**
     * 根据传入的路径生成uri
     */
    private static Uri getUri(String path) {
        File directory = new File(Environment.getExternalStorageDirectory() + path + File.separator);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        int randomNum = new Random().nextInt(100000) + 899999;
        String filename = FORMAT.format(new Date()) + randomNum + ".jpg";
        File file = new File(directory, filename);
        return Uri.fromFile(file);
    }

    /**
     * 根据uri获得实际路径
     */
    public static String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = BaseApplication.Companion.getContext().getContentResolver().query(contentURI,
                new String[]{MediaStore.Images.ImageColumns.DATA},
                null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data, PictureCallback callback) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ALBUM:
                       if (data != null && null != data.getData()) {
                        String realPath = getRealPathFromURI(data.getData());
                        callback.albumCallback(realPath);
                    } else {
                        ToastUtil.showShort("获取图片失败");
                    }
                    break;
                case CAMERA:
                    callback.cameraCallback();
                    break;
                case CROP:
                    if (data != null && null != data.getData()) {
                        String realPath = getRealPathFromURI(data.getData());
                        callback.cropCallback(realPath);
                    } else {
                        ToastUtil.showShort("拍照取消");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Activity初始化时调用，防止在7.0及以上版本报异常
     */
    public static void initPhotoError() {
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    public static void flushPhoto(String filePath) {
        File file = new File(filePath);
        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        MediaStore.Images.Media.insertImage(BaseApplication.Companion.getContext().getContentResolver(),
                bmp, file.getName(), null);
        bmp.recycle();
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        BaseApplication.Companion.getContext().sendBroadcast(intent);
    }


    public static abstract class PictureCallback {

        public void albumCallback(String imagePath){};

        public void cameraCallback(){};

        public void cropCallback(String imagePath){};

    }

}
