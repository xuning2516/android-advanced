package com.royole.androidcommon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 *@url https://github.com/ifadai/PermissionUtils
 */
public class PermissionUtils {


    /**
     * 检测权限
     *
     * @return true：已授权； false：未授权；
     */
    public static boolean checkPermissions(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    /**
     * 检测多个权限
     *
     * @return 未授权的权限
     */
    public static List<String> checkPermissions(Context context, String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (!checkPermissions(context, permissions[i]))
                permissionList.add(permissions[i]);
        }
        return permissionList;
    }


    /**
     * 请求多个权限
     */
    public static void requestPermissions(Context context, List permissionList, int requestCode) {
        String[] permissions = (String[]) permissionList.toArray(new String[permissionList.size()]);
        requestPermissions(context, permissions, requestCode);
    }

    /**
     * 请求多个权限
     */
    public static void requestPermissions(Context context, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, permissions, requestCode);
    }

    /**
     * 判断是否已拒绝过权限
     *
     * @return
     * @describe :如果应用之前请求过此权限但用户拒绝，此方法将返回 true;
     * -----------如果应用第一次请求权限或 用户在过去拒绝了权限请求，
     * -----------并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。
     */
    public static boolean judgePermission(Context context, String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission))
            return true;
        else
            return false;
    }

    /**
     * 请求权限
     */
    public static void requestPermissions(Context context, String permission, int requestCode) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
    }

    /**
     * @deprecated
     * 检测权限并请求权限：如果没有权限，则请求权限
     */
    public static void checkAndRequestPermissions(Context context, String permission, int requestCode) {
        if (!checkPermissions(context, permission)) {
            requestPermissions(context, permission, requestCode);
        }
    }

    /**
     * @deprecated
     * 检测并请求多个权限
     */
    public static void checkAndRequestPermissions(Context context, String[] permissions, int requestCode) {
        List<String> permissionList = checkPermissions(context, permissions);
        requestPermissions(context, permissionList, requestCode);
    }


    /**
     * 检测权限
     *
     * @describe：具体实现由回调接口决定
     */
    public static void checkPermissions(Context context, String permission, PermissionCheckCallBack callBack) {
        if (checkPermissions(context, permission)) { // 用户已授予权限
            callBack.onGranted();
        } else {
            if (judgePermission(context, permission))  // 用户之前已拒绝过权限申请
                callBack.onDeniedDontAsk(permission);
            else                                       // 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
                callBack.onDenied(permission);
        }
    }

    /**
     * 检测多个权限
     *
     * @describe：具体实现由回调接口决定
     */
    public static void checkPermissions(Context context, String[] permissions, PermissionCheckCallBack callBack) {
//        List<String> permissionList = checkPermissions(context, permissions);
//        if (permissionList.size() == 0) {  // 用户已授予权限
//            callBack.onGranted();
//        } else {
//            boolean isFirst = true;
//            for (int i = 0; i < permissionList.size(); i++) {
//                String permission = permissionList.get(i);
//                if (judgePermission(context, permission)) {
//                    isFirst = false;
//                    break;
//                }
//            }
//            String[] unauthorizedMorePermissions = (String[]) permissionList.toArray(new String[permissionList.size()]);
//            if (isFirst)// 用户之前已拒绝过权限申请
//                callBack.onDenied(unauthorizedMorePermissions);
//            else       // 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
//                callBack.onDeniedDontAsk(unauthorizedMorePermissions);
//
//        }

        int grantedCount = 0;
        List<String> deniedpermission = new ArrayList<>();
        List<String> deniedNoAskpermission = new ArrayList<>();
        for (String permission:permissions) {
            if (checkPermissions(context, permission)) { // 用户已授予权限
                //callBack.onGranted();
                grantedCount ++;
            } else {
                if (judgePermission(context, permission)) { // 用户之前已拒绝过权限申请
                    //callBack.onDenied(permission);
                    deniedNoAskpermission.add(permission);
                }
                else {                                  // 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
                    //callBack.onDeniedDontAsk(permission);
                    deniedpermission.add(permission);
                }
            }
        }
        if(grantedCount == permissions.length){
            callBack.onGranted();
        }else if(deniedpermission.size() > 0){
            callBack.onDenied((String[])deniedpermission.toArray());
        }else if(deniedNoAskpermission.size() > 0){
            callBack.onDeniedDontAsk((String[])deniedNoAskpermission.toArray());
        }
    }


    /**
     * 检测并申请权限
     */
    public static void checkAndRequestPermission(Context context, String permission, int requestCode, PermissionRequestSuccessCallBack callBack) {
        if (checkPermissions(context, permission)) {// 用户已授予权限
            callBack.onGranted();
        } else {
            requestPermissions(context, permission, requestCode);
        }
    }

    /**
     * 检测并申请多个权限
     */
    public static void checkAndRequestPermissions(Context context, String[] permissions, int requestCode, PermissionRequestSuccessCallBack callBack) {
        List<String> permissionList = checkPermissions(context, permissions);
        if (permissionList.size() == 0) {  // 用户已授予权限
            callBack.onGranted();
        } else {
            requestPermissions(context, permissionList, requestCode);
        }
    }

    /**
     * 判断权限是否申请成功
     */
    public static boolean isPermissionRequestSuccess(int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    /**
     * 用户申请权限返回
     */
    public static void onRequestPermissionsResult(Context context, String permission, int[] grantResults, PermissionCheckCallBack callback) {
        if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
            callback.onGranted();
        } else {
            if (PermissionUtils.judgePermission(context, permission)) {
                callback.onDeniedDontAsk(permission);
            } else {
                callback.onDenied(permission);
            }
        }
    }

    /**
     * 用户申请多个权限返回
     */
    public static void onRequestPermissionsResult(Context context, String[] permissions, int[] grantResults,PermissionCheckCallBack callback) {
//        boolean isBannedPermission = false;
//        List<String> permissionList = checkPermissions(context, permissions);
//        if (permissionList.size() == 0)
//            callback.onGranted();
//        else {
//            for (int i = 0; i < permissionList.size(); i++) {
//                if (!judgePermission(context, permissionList.get(i))) {
//                    isBannedPermission = true;
//                    break;
//                }
//            }
//            //　已禁止再次询问权限
//            if (isBannedPermission)
//                callback.onUserHasAlreadyTurnedDownAndDontAsk(permissions);
//            else // 拒绝权限
//                callback.onUserHasAlreadyTurnedDown(permissions);
//        }

        int grantedCount = 0;
        List<String> deniedpermission = new ArrayList<>();
        List<String> deniedNoAskpermission = new ArrayList<>();


        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0) {

            for (int i=0; i<permissions.length; ++i) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) { // 用户已授予权限
                    //callBack.onGranted();
                    grantedCount ++;
                } else {
                    if (judgePermission(context, permission)) { // 用户之前已拒绝过权限申请
                        //callBack.onDenied(permission);
                        deniedNoAskpermission.add(permission);
                    }
                    else {                                  // 用户之前已拒绝并勾选了不在询问、用户第一次申请权限。
                        //callBack.onDeniedDontAsk(permission);
                        deniedpermission.add(permission);
                    }
                }
            }
            if(grantedCount == permissions.length){
                callback.onGranted();
            }else if(deniedpermission.size() > 0){
                callback.onDenied((String[])deniedpermission.toArray());
            }else if(deniedNoAskpermission.size() > 0){
                callback.onDeniedDontAsk((String[])deniedNoAskpermission.toArray());
            }

        } else {
            // If request is cancelled, the result arrays are empty.

        }
    }


    /**
     * 跳转到权限设置界面
     */
    public static void toAppSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    public interface PermissionRequestSuccessCallBack {
        /**
         * 用户已授予权限
         */
        void onGranted();
    }


    public interface PermissionCheckCallBack {

        /**
         * 用户已授予权限
         */
        void onGranted();

        /**
         * 用户已拒绝过权限
         *
         * @param permission:被拒绝的权限
         */
        void onDenied(String... permission);

        /**
         * 用户已拒绝过并且已勾选不再询问选项、用户第一次申请权限;
         *
         * @param permission:被拒绝的权限
         */
        void onDeniedDontAsk(String... permission);
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
