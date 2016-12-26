package com.cxmx.statisticalsdk.base;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cxmx.statisticalsdk.Statistical;
import com.cxmx.statisticalsdk.dataInterface.StatisticalInterface;
import com.cxmx.statisticalsdk.utils.HttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

import okhttp3.Call;

/**
 * 作者：xxq on 2016/8/5 16:26
 * 基础的网络请求
 */
public class BaseModel {
    /**
     * 得到上下文
     */
    public static Context getContext() {
        return Statistical.mContext;
    }

    /**
     * 公共方法
     * 将请求结果放到 MyInterface 中
     *
     * @param response
     * @param myInterface
     */
    public static void setMyInterface(String response, StatisticalInterface myInterface) {
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            int ret = jsonObject.getInteger("ret");
            String data = jsonObject.getString("data");
            //返回ret状态码、data数据 、和jsonObject 。
            myInterface.onSucceed(ret, data, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 公共请求的
     *
     * @param URL         地址
     * @param hashMap
     * @param myInterface
     */
    public static void initHttp(String URL, HashMap<String, String> hashMap, final StatisticalInterface myInterface) {
        HttpUtils.postStringAsync(URL, hashMap, getContext(), new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                myInterface.onError(call, e);
            }

            @Override
            public void onResponse(String response, int id) {
                setMyInterface(response, myInterface);
            }
        });

    }
}
