package com.cxmx.statisticalsdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.cxmx.statisticalsdk.base.BaseModel;
import com.cxmx.statisticalsdk.config.AppServerUrl;
import com.cxmx.statisticalsdk.dataInterface.StatisticalInterface;
import com.cxmx.statisticalsdk.utils.PackageUtil;
import com.cxmx.statisticalsdk.utils.XUtilNet;
import com.cxmx.statisticalsdk.utils.jm.JmTools;

import java.util.HashMap;

/**
 * Created by lxh on 2016/12/20.
 * QQ-632671653
 * 提供外部调用统计接口
 */

public class Statistical extends BaseModel {

    private static final String TAG = "Statistical";
    public static Context mContext;
    public static String JMUSERKEY;//加密的key  例如：cxmxdjcc
    private static String APP_KEY;
    private static String CHANNEL;
    private static boolean isInit = false;

    /**
     * 统计初始化
     * @param context 上下文
     * @param app_key  标识app  例如 21
     * @param channel  渠道 如 "UMENG_CHANNEL"
     */
    public static void initSDK(Context context,String app_key,String channel){
        mContext = context.getApplicationContext();
//        JMUSERKEY = jmKey;
        APP_KEY = app_key;
        CHANNEL = channel;
        isInit = true;
    }

    /**
     * 统计第一次安装打开
     *
     * @param myInterface
     */
    public static void FirstInstall(StatisticalInterface myInterface) {
        if (!isInit){
            Log.e(TAG,"请先进行初始化");
            return;
        }
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String t = System.currentTimeMillis() + "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("app_key", APP_KEY);
        jsonObject.put("qid", getqid(mContext,CHANNEL));
        jsonObject.put("imei", android.os.Build.SERIAL);
        jsonObject.put("phone_name", Build.MANUFACTURER + Build.DEVICE);
        jsonObject.put("internet", XUtilNet.getNetType());
        jsonObject.put("intertype", tm.getSimOperatorName());
        jsonObject.put("appcount", PackageUtil.getAllAppNumNoSystem(mContext));
        jsonObject.put("applist", PackageUtil.getAllAppInfoNoSystem(mContext));
        jsonObject.put("app_version", PackageUtil.getAppVersionName(mContext, mContext.getPackageName()));
        jsonObject.put("phone_model", Build.MODEL);
        jsonObject.put("system_version", Build.VERSION.RELEASE);
        jsonObject.put("type", "1");
        jsonObject.put("contype", "1");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("service", "Firstlogin.Newindex");
        hashMap.put("T", t);
        hashMap.put("data", JmTools.encryptionEnhanced(t, jsonObject.toString()));
        //必须放在最后面 ， 改下方法名即可。
        initHttp(AppServerUrl.Firstlogin_Newindex, hashMap, myInterface);
    }

    /**
     * 统计app打开次数
     * @param myInterface
     */
    public static void OpenApp(StatisticalInterface myInterface) {
        if (!isInit){
            Log.e(TAG,"请先进行初始化");
            return;
        }
        String t = System.currentTimeMillis() + "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("app_key", APP_KEY);
        jsonObject.put("qid", getqid(mContext,CHANNEL));
        jsonObject.put("imei", android.os.Build.SERIAL);
        jsonObject.put("internet", XUtilNet.getNetType());
        jsonObject.put("app_version", PackageUtil.getVersionName(mContext));
        jsonObject.put("type", "1");
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("service", "Openapp.Newindex");
        hashMap.put("T", t);
        hashMap.put("data", JmTools.encryptionEnhanced(t, jsonObject.toString()));
        //必须放在最后面 ， 改下方法名即可。
        initHttp(AppServerUrl.Openapp_Newindex, hashMap, myInterface);
    }

    /**
     * 异常统计
     * @param exceptionMSG
     * @param myInterface
     */
    public static void CatchUpload(String exceptionMSG,StatisticalInterface myInterface) {
        if (!isInit){
            Log.e(TAG,"请先进行初始化");
            return;
        }
        String t = System.currentTimeMillis() + "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("app_key", APP_KEY);
        jsonObject.put("qid", getqid(mContext,CHANNEL));
        jsonObject.put("imei", android.os.Build.SERIAL);
        jsonObject.put("phone_name", Build.MANUFACTURER + Build.DEVICE);
        jsonObject.put("phone_model", Build.MODEL);
        jsonObject.put("system_version", Build.VERSION.RELEASE);
        jsonObject.put("app_version", PackageUtil.getVersionName(mContext));
        jsonObject.put("internet", XUtilNet.getNetType());
        jsonObject.put("type", "1");
        jsonObject.put("msg", exceptionMSG);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("service", "Openapp.Newindex");
        hashMap.put("T", t);
        hashMap.put("data", JmTools.encryptionEnhanced(t, jsonObject.toString()));
        //必须放在最后面 ， 改下方法名即可。
        initHttp(AppServerUrl.Catchs_Newindex, hashMap, myInterface);
    }


    /**
     * 初始化程序
     * 获取打包渠道ID
     */

    public static String getqid(Context context,String channel) {
        ApplicationInfo info;
        try {
            info = context.getApplicationContext().getPackageManager().getApplicationInfo(context.getApplicationContext().getPackageName(),
                    PackageManager.GET_META_DATA);
            if (channel == null){
                channel = "UMENG_CHANNEL";
            }
            String msg = info.metaData.getString(channel);
            return msg;
        } catch (Exception e) {
            return "";
        }
    }

}
