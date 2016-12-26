package com.cxmx.statisticalsdk.utils.jm;

import com.alibaba.fastjson.JSONObject;
import com.cxmx.statisticalsdk.Statistical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wbb on 2016/4/6.
 */
public class JmTools {

    public final static String DKAETYA = "@#-*￥cxmx^&+#@*";

    /**
     * 输入加密 T
     * @param str
     * @return
     */
    public static String DKAETYA16(String str){

        String a=str.substring(0, 16);

        return a;
    }

    /**
     * json 解密
     * @param object
     * @return
     */
    public static String DecryptKey(JSONObject object){

        String result = object.getString("result");

        String t = object.getString("T");

        String keyString= Security.md5(t + DKAETYA);

        String key=DKAETYA16(keyString);

        //解密
        String data=Security.decrypt(key, result);

        return data;
    }

    /**
     * 字符串 加密
     * @param time
     * @param mString
     * @return
     */
    public static String encryptionEnhanced(String time,String mString){

        String key=Security.md5(time+DKAETYA);

        String key16=DKAETYA16(key);

        String date=Security.encrypt(key16,mString);

        return date;
    }

    /**
     * 加密 url signkey
     *
     * @param hashMap
     * @param method
     * @return
     */
    public static String NRJM(HashMap<String, String> hashMap, String method) {
        List<String> listKey = new ArrayList();
        Iterator it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            listKey.add(key);
        }
        List<String> listSort = Sorting.listSort(listKey);
        String key = "";
        StringBuilder sb = new StringBuilder();
        for (String string : listSort) {
            String name = hashMap.get(string);
            key = sb.append(name).toString();
        }
        return Security.md5(key + method + Statistical.JMUSERKEY);
    }

    /**
     * 用户加密
     *
     * @param hashMap
     * @param methond
     * @return
     */
    public static String USER_NRJM(HashMap<String, String> hashMap, String methond) {
        hashMap.put("service", methond);
        List<String> listKey = new ArrayList();
        Iterator it = hashMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            listKey.add(key);
        }
        List<String> listSort = Sorting.listSort(listKey);
        String key = "";
        StringBuilder sb = new StringBuilder();
        for (String string : listSort) {
            String name = hashMap.get(string);
            key = sb.append(name).toString();
        }
        return Security.md5(key + Statistical.JMUSERKEY);
    }

}
