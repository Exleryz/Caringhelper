package com.weimore.util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

/**
 * @author Weimore
 *         2018/12/11.
 *         description:
 */

public class PermissionUtil {

    private final static int REQ_CODE = 200;

    public interface PermissionsCallback {
        void success();
    }

    public static void permissionRequest(final Context context, final PermissionsCallback callback, final String... permissions) {
        permissionRequest(context,null,callback, permissions);
    }

    public static void permissionRequest(final Context context, final String message,final PermissionsCallback callback,
                                         final String... permissions) {
        AndPermission.with(context)
                .requestCode(REQ_CODE)
                .permission(permissions)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        if (requestCode == REQ_CODE) {
                            // 申请权限成功
                            if (AndPermission.hasPermission(context, permissions)) {
                                callback.success();
                            } else {
                                //用AndPermission默认的提示语。
                                if (message != null) {
                                    AndPermission.defaultSettingDialog((Activity) context, requestCode).setMessage(message).show();
                                } else {
                                    AndPermission.defaultSettingDialog((Activity) context, requestCode).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        if (requestCode == REQ_CODE) {
                            // 申请权限失败
                            if (AndPermission.hasPermission(context, permissions)) {
                                callback.success();
                            } else {
                                //用AndPermission默认的提示语。
                                if (message != null) {
                                    AndPermission.defaultSettingDialog((Activity) context, requestCode).setMessage(message).show();
                                } else {
                                    AndPermission.defaultSettingDialog((Activity) context, requestCode).show();
                                }
                            }
                        }
                    }
                })
                .start();
    }

}
