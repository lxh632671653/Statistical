package com.cxmx.statisticalsdk.dataInterface;

import com.alibaba.fastjson.JSONObject;

import okhttp3.Call;

/**
 * 作者：Administrator on 2016/8/2 15:20
 */
public interface StatisticalInterface {
    /**
     * 接口请求失败
     *
     * @param call
     * @param e    异常信息
     */
    void onError(Call call, Exception e);

    /**
     * 接口请求返回成功
     *
     * @param state 状态码
     * @param data  返回data数据
     * @param obj   JSONObject
     */
    void onSucceed(int state, String data, JSONObject obj);

}
