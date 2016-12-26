package com.cxmx.statisticalsdk.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class PackageUtil {

    private static final String TAG = "PackageUtil";

    /**
     * 卸载apk
     *
     * @param context
     * @param pkg     包名
     */
    public static void startUninstall(Context context, String pkg) {
        Uri pkgUri = Uri.parse("package:" + pkg);
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(pkgUri);
        context.startActivity(intent);
    }

    public static JSONObject[] getAllAppInfoNoSystem(Context context){
        List<JSONObject> appInfo = getAllAppsNoSystem(context);
        int listSize = appInfo.size();
        JSONObject[] jsonObjects = new JSONObject[listSize];
        for (int i = 0;i<listSize;i++){
            jsonObjects[i] = appInfo.get(i);
        }
        return jsonObjects;
    }

    /**
     * 查询手机内非系统应用
     * @param context
     * @return
     */
    public static List<JSONObject> getAllAppsNoSystem(Context context) {
        List<JSONObject> appPackageName = new ArrayList<JSONObject>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                String appName = pak.applicationInfo.loadLabel(pManager).toString();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("appName",appName);
                jsonObject.put("packageName",pak.packageName);
                appPackageName.add(jsonObject);
            }
        }
        return appPackageName;
    }



    public static JSONObject[] getInstalledApplications(Context context) {
        // 获取到包的管理者
        PackageManager packageManager = context.getPackageManager();
        // 获取所有的安装程序
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);

        for (int i = 0; i < installedPackages.size(); i++) {

            PackageInfo info = installedPackages.get(i);
            ApplicationInfo applicationInfo = info.applicationInfo;

            // 获取程序的所有标签 用来获取 以下信息
            int flags = applicationInfo.flags;
            // 判断是不是用户程序
            if ((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                // 系统程序
                installedPackages.remove(info);
            }
        }

        JSONObject[] packageNames = new JSONObject[installedPackages.size()];
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo info = installedPackages.get(i);
            ApplicationInfo applicationInfo = info.applicationInfo;
            String packageName = info.packageName;
            String appName = applicationInfo.loadLabel(packageManager).toString();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("packageName", packageName);
            jsonObject.put("appName", appName);
            packageNames[i] = jsonObject;
        }
        return packageNames;
    }

    /**
     * 查询手机内不包含系统应用的app总数
     * @param context
     * @return
     */
    public static int getAllAppNumNoSystem(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps.size();
    }

    /**
     * 安装APK
     *
     * @param context
     * @param apkPath
     */
    public static void startInstall(Context context, String apkPath) {
        Uri uri = Uri.parse("file://" + apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 启动APP
     * @param context
     * @param pkg
     */
    public static boolean startApp(Context context, String pkg) {
        try {
            PackageManager pm = context.getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage(pkg);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e(TAG,e.toString());

        }

        return false;
    }

    /**
     * 获取客户端版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }

    /**
     * 获取APP版本号
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context, String pkg) {
        if (TextUtils.isEmpty(pkg))
            return -1;

        try {
            return context.getPackageManager().getPackageInfo(pkg, 0).versionCode;
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * 获取客户端版本名
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        return getAppVersionName(context, context.getPackageName());
    }

    /**
     * 获取APP版本名
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context, String pkg) {
        if (TextUtils.isEmpty(pkg))
            return "";
        try {
            return context.getPackageManager().getPackageInfo(pkg, 0).versionName;
        } catch (Exception e) {
        }
        return "";
    }


    /**
     * APK是否合法
     * @param context
     * @param file
     * @return
     */
//    public static boolean isApkValid(Context context, File file) {
//        return queryPackageInfo(context, file) != null;
//    }

    /**
     * APP是否可以打开 (是否有启动的MainActivity)
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAPPCanLaunch(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo pi;
        boolean flag = false;
        try {
            pi = packageManager.getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(pi.packageName);
            List<ResolveInfo> apps = packageManager.queryIntentActivities(resolveIntent, 0);
            if (apps != null && apps.size() > 0)
                flag = true;
        } catch (Exception e) {
            Log.e(TAG,e.toString());

        }
        return flag;
    }


    /**
     * 根据包名获取文件的包信息
     * @param context
     * @param packageName
     * @return
     */
    public static PackageInfo queryPackageInfo(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return null;
        }
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        return packageInfo;
    }

    /**
     * 根据文件路径获取文件的包信息
     *
     * @param context
     * @param file
     * @return
     */
//    public static PackageInfo queryPackageInfo(Context context, File file) {
//        if (!FileUtil.isFileExist(file) || TextUtils.isEmpty(file.getAbsolutePath())) {
//            return null;
//
//        } else {
//            try {
//                PackageManager pm = context.getPackageManager();
//                PackageInfo info = pm.getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
//                if (info != null) {
//                    return info;
//                }
//            } catch (Exception e) {
//            }
//
//        }
//        return null;
//
//    }

    /**
     * app是否安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstall(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null || TextUtils.isEmpty(packageName)) {
            return false;
        }
        PackageInfo packageInfo = queryPackageInfo(context, packageName);
        return packageInfo != null;
    }

    public static boolean isAppInstall(Context context, String packageName, int code) {
        int orCode = getAppVersionCode(context, packageName);
        PackageInfo packageInfo = queryPackageInfo(context, packageName);
        if (packageInfo == null) {
            return false;
        } else {

            if(code == orCode){
                return true;
            }
            return false;
        }
    }

    public static boolean isAppInstall(Context context, String packageName, String name) {
        String orName = getAppVersionName(context, packageName);
        PackageInfo packageInfo = queryPackageInfo(context, packageName);
        if (packageInfo == null) {
            return false;
        } else {
            if(name.equals(orName)){
                return true;
            }
            return false;
        }
    }

    /**
     * 获取APK文件的Appname
     * @param ctx
     * @param apkPath
     * @return
     */
    public static String getApkFileTitle(Context ctx, String apkPath) {
        ApplicationInfo applicationInfo = getApkFileApplicationInfo(apkPath);
        Resources resources = getApkFileResources(ctx, apkPath);
        if (resources != null && applicationInfo != null){
            return resources.getText(applicationInfo.labelRes).toString();
        }
        return "";
    }

    /**
     * 获取APK文件的icon
     * @param ctx
     * @param apkPath
     * @return
     */
    public static Drawable getApkFileIcon(Context ctx, String apkPath) {
        ApplicationInfo applicationInfo = getApkFileApplicationInfo(apkPath);
        Resources resources = getApkFileResources(ctx, apkPath);
        if(resources != null && applicationInfo != null)
            return resources.getDrawable(applicationInfo.icon);

        return null;
    }

    public static Resources getApkFileResources(Context ctx, String apkPath){
        String PATH_AssetManager = "android.content.res.AssetManager";
        Resources res = null;
        try {
            Class assetMagCls = Class.forName(PATH_AssetManager);
            Constructor assetMagCt = assetMagCls.getConstructor((Class[]) null);
            Object assetMag = assetMagCt.newInstance((Object[]) null);
            Class[] typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", typeArgs);
            Object[] valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
            res = ctx.getResources();
            typeArgs = new Class[3];
            typeArgs[0] = assetMag.getClass();
            typeArgs[1] = res.getDisplayMetrics().getClass();
            typeArgs[2] = res.getConfiguration().getClass();
            Constructor resCt = Resources.class.getConstructor(typeArgs);
            valueArgs = new Object[3];
            valueArgs[0] = assetMag;
            valueArgs[1] = res.getDisplayMetrics();
            valueArgs[2] = res.getConfiguration();
            res = (Resources) resCt.newInstance(valueArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static ApplicationInfo getApkFileApplicationInfo(String apkPath) {
        String PATH_PackageParser = "android.content.pm.PackageParser";
        try {
            // apk包的文件路径
            // 这是一个Package 解释器, 是隐藏的
            // 构造函数的参数只有一个, apk文件的路径
            // PackageParser packageParser = new PackageParser(apkPath);
            Class pkgParserCls = Class.forName(PATH_PackageParser);
            Constructor pkgParserCt;
            Class[] typeArgs;
            if (Build.VERSION.SDK_INT >= 20) {
                pkgParserCt = pkgParserCls.getConstructor();
            } else {
                typeArgs = new Class[1];
                typeArgs[0] = String.class;
                pkgParserCt = pkgParserCls.getConstructor(typeArgs);
            }
            Object[] valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            Object pkgParser;
            if (Build.VERSION.SDK_INT >= 20) {
                pkgParser = pkgParserCt.newInstance();
            } else {
                pkgParser = pkgParserCt.newInstance(valueArgs);
            }
            // 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();
            // PackageParser.Package mPkgInfo = packageParser.parsePackage(new
            // File(apkPath), apkPath,
            // metrics, 0);
            if (Build.VERSION.SDK_INT >= 20) {
                typeArgs = new Class[2];
                typeArgs[0] = File.class;
                typeArgs[1] = Integer.TYPE;
            } else {
                typeArgs = new Class[4];
                typeArgs[0] = File.class;
                typeArgs[1] = String.class;
                typeArgs[2] = DisplayMetrics.class;
                typeArgs[3] = Integer.TYPE;
            }

            Method pkgParser_parsePackageMtd;

            if (Build.VERSION.SDK_INT >= 20) {
                typeArgs = new Class[2];
                typeArgs[0] = File.class;
                typeArgs[1] = Integer.TYPE;
                pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parseMonolithicPackage", typeArgs);
            } else {
                typeArgs = new Class[4];
                typeArgs[0] = File.class;
                typeArgs[1] = String.class;
                typeArgs[2] = DisplayMetrics.class;
                typeArgs[3] = Integer.TYPE;
                pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage", typeArgs);
            }


            Object pkgParserPkg = null;
            if (Build.VERSION.SDK_INT >= 20) {
                valueArgs = new Object[2];
                valueArgs[0] = new File(apkPath);
                valueArgs[1] = 0;
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);

            } else {

                valueArgs = new Object[4];
                valueArgs[0] = new File(apkPath);
                valueArgs[1] = apkPath;
                valueArgs[2] = metrics;
                valueArgs[3] = 0;
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);
            }

            // 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
            // ApplicationInfo info = mPkgInfo.applicationInfo;
            Field appInfoFld = pkgParserPkg.getClass().getDeclaredField("applicationInfo");
            return (ApplicationInfo) appInfoFld.get(pkgParserPkg);
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
        return null;
    }

}