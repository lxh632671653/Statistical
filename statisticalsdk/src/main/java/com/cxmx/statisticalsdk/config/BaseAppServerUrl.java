package com.cxmx.statisticalsdk.config;

/**
 * Created by wbb on 2016/3/24.
 */
public class BaseAppServerUrl {

    //测试 http://testapi.tongji.com/
    public static String statisticsUrl = "http://testapi.tongji.com/";

    /*统计接口*/
    public static String getStatisticsUrl() {
        return BaseAppServerUrl.statisticsUrl + "?service=";
    }

}
