package com.lxh.statistical;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.alibaba.fastjson.JSONObject;
import com.cxmx.statisticalsdk.Statistical;
import com.cxmx.statisticalsdk.dataInterface.StatisticalInterface;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    Button installStatisticalTest,openStatisticalTest,exceptionStatisticalTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 统计初始化
         * @param context 上下文
         * @param app_key  标识app  例如 21      根据不同项目找后台沟通
         * @param channel  渠道 如 "UMENG_CHANNEL"
         */
        Statistical.initSDK(this,"21","UMENG_CHANNEL");
        installStatisticalTest = (Button) findViewById(R.id.installStatisticalTest);
        openStatisticalTest = (Button) findViewById(R.id.openStatisticalTest);
        exceptionStatisticalTest = (Button) findViewById(R.id.exceptionStatisticalTest);
        installStatisticalTest.setOnClickListener(this);
        openStatisticalTest.setOnClickListener(this);
        exceptionStatisticalTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.installStatisticalTest:
                Statistical.FirstInstall(new StatisticalInterface() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e(TAG,"=======请求失败======="+e);
                    }

                    @Override
                    public void onSucceed(int state, String data, JSONObject obj) {
                        Log.e(TAG,"=======请求成功======="+"=state="+state+"=data="+data);
                    }
                });
                break;
            case R.id.openStatisticalTest:
                Statistical.OpenApp(new StatisticalInterface() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e(TAG,"=======请求失败======="+e);
                    }

                    @Override
                    public void onSucceed(int state, String data, JSONObject obj) {
                        Log.e(TAG,"=======请求成功======="+"=state="+state+"=data="+data);
                    }
                });
                break;
            case R.id.exceptionStatisticalTest:
                Statistical.CatchUpload("此处是异常信息！！！", new StatisticalInterface() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.e(TAG,"=======请求失败======="+e);
                    }

                    @Override
                    public void onSucceed(int i, String s, JSONObject jsonObject) {
                        Log.e(TAG,"=======请求成功======="+"=state="+i+"=data="+s);
                    }
                });
                break;
        }
    }
}
