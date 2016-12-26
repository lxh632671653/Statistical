package com.cxmx.statisticalsdk.config;

/**
 * Created by wbb on 2016/4/27.
 */
public abstract class AppServerUrl extends BaseAppServerUrl {

    //统计第一次安装
    public final static String Firstlogin_Newindex = getStatisticsUrl() + "Firstlogin.Newindex";

    //统计APP打开次数
    public final static String Openapp_Newindex = getStatisticsUrl() + "Openapp.Newindex";

    //统计APP打开次数
    public final static String Catchs_Newindex = getStatisticsUrl() + "Catchs.Newindex";

}
