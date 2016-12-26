package com.cxmx.statisticalsdk.utils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Response;

/**
 * Created by lijian on 2016/3/31.
 * <p/>
 * 这是简单封装过的网络请求工具类  有同步和异步两种请求模式
 * StringCallback是字符串请求和上传结果的回调
 * BitmapCallback是图片请求结果的回调
 * FileCallback是文件下载的回调
 * 处理结果请在回调方法中进行
 * 需要使用进度的话需要重写回调中的inProgress方法
 *
 * @Override public void inProgress(float progress)
 * {
 * //use progress: 0 ~ 1
 * }
 * <p/>
 * <p/>
 * 取消任务的方式是  OkHttpUtils.getInstance().cancelTag( tag);这样可以根据标记来取消所有带此tag的任务
 */
public class HttpUtils {

    //get方式同步请求字符串
    public static String getStringSync(String url) {


        if (XUtilNet.isNetConnected()) {
            BufferedReader br = null;
            try {

                Response response = OkHttpUtils.get().url(url).build().execute();
                br = new BufferedReader(response.body().charStream());
                return br.readLine();

            } catch (IOException e) {
                return null;
            } finally {
                IOUtils.close(br);
            }
        }
        return null;
    }

    //get方式异步请求字符串
    public static void getStringAsync(String url, Object tag, StringCallback callback) {
        if (XUtilNet.isNetConnected()) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .tag(tag)
                    .build()
                    .execute(callback);
        }
    }

    //post方式异步请求字符串
    public static void postStringAsync(String url, HashMap<String, String> params, Object tag, StringCallback callback) {
        if (XUtilNet.isNetConnected()) {
//            OkHttpClient client = new OkHttpClient();
//            client.networkInterceptors().add(new StethoInterceptor());
            PostFormBuilder builder = OkHttpUtils.post().url(url).tag(tag);

            if (params != null) {
                Set<Map.Entry<String, String>> entries = params.entrySet();
                for (Map.Entry<String, String> ele : entries) {
                    builder.addParams(ele.getKey(), ele.getValue());
                }
            }
            builder.build().execute(callback);
        }
    }

    //post方式同步请求字符串
    public static String postStringSync(String url, HashMap<String, String> params) {
        if (XUtilNet.isNetConnected()) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            PostFormBuilder builder = OkHttpUtils.post().url(url);
            BufferedReader br = null;
            for (Map.Entry<String, String> ele : entries) {
                builder.addParams(ele.getKey(), ele.getValue());
            }
            try {
                Response response = builder.build().execute();
                br = new BufferedReader(response.body().charStream());
                return br.readLine();
            } catch (IOException e) {
                return null;
            } finally {
                IOUtils.close(br);
            }
        }
        return null;
    }


    //get方式异步请求图片
    public static void getBitmapAsync(String url, Object tag, BitmapCallback callback) {
        if (XUtilNet.isNetConnected()) {
            OkHttpUtils
                    .get()
                    .url(url)
                    .tag(tag)
                    .build()
                    .execute(callback);
        }
    }

    //上传文件
    public static void uploadFile(String url, File file, Object tag, StringCallback resultCallback) {
        if (XUtilNet.isNetConnected()) {
            OkHttpUtils
                    .postFile()
                    .url(url)
                    .file(file)
                    .tag(tag)
                    .build()
                    .execute(resultCallback);
        }
    }

    /**
     * Post请求上传文件
     *
     * @param url
     * @param fileName
     * @param file
     * @param params
     * @param resultCallback
     */
    public static void uploadFile(String url, String fileName, File file, HashMap<String, String> params, StringCallback resultCallback) {
        if (XUtilNet.isNetConnected()) {
            OkHttpUtils.post()//
                    .addFile("mFile", fileName, file)//
                    .url(url)
                    .params(params)//
                    .build()//
                    .execute(resultCallback);
        }
    }

    public static void uploadFile(String url, String filePath, Object tag, StringCallback resultCallback) {
        {
            File file = new File(filePath);
            OkHttpUtils
                    .postFile()
                    .url(url)
                    .file(file)
                    .tag(tag)
                    .build()
                    .execute(resultCallback);
        }
    }

    //上传字符串
    public static void uploadString(String url, String jsonString, Object tag, StringCallback resultCallback) {
        {
            OkHttpUtils
                    .postString()
                    .url(url)
                    .content(jsonString)
                    .build()
                    .execute(resultCallback);
        }
    }

    public static void downloadNewSelf(String url, FileCallBack callBack) {
        if (XUtilNet.isNetConnected()) {
            OkHttpUtils.get()
                    .url(url)
                    .build()
                    .execute(callBack);
        } else {
//            XUtilToast.showToast("当前没有网络连接");
        }
    }

}
