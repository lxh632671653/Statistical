package com.cxmx.statisticalsdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import com.cxmx.statisticalsdk.Statistical;


public class XUtilNet {

	private static String NETTYPE_2g = "2G";
	private static String NETTYPE_3g = "3G";
	private static String NETTYPE_4g = "4G";
	private static String NETTYPE_wifi = "wifi";
	private static String NETTYPE_NoCoonnected = "no connected";
	private static Context context = Statistical.mContext;

	/**
	 * 检测网络是否连接
	 * 
	 * @return
	 */
	public static boolean isNetConnected() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo[] infos = cm.getAllNetworkInfo();
			if (infos != null) {
				for (NetworkInfo ni : infos) {
					if (ni.isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 检测wifi是否连接
	 * 
	 * @return true false
	 */
	public static boolean isWifiConnected() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检测3G是否连接
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public static boolean is3gConnected() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null
					&& networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取网络状态
	 * @return
     */
	public static String getNetType() {
		String netType = "";
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (isNetConnected()) {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null) {
				switch (networkInfo.getType()) {
					case ConnectivityManager.TYPE_WIFI:
						netType = NETTYPE_wifi;
						break;
					case ConnectivityManager.TYPE_MOBILE:
						if (networkInfo.getSubtype() == telephonyManager.NETWORK_TYPE_1xRTT) {
							netType = NETTYPE_2g;
							break;
						} else if (networkInfo.getSubtype() == telephonyManager.NETWORK_TYPE_HSPAP) {
							netType = NETTYPE_3g;
							break;
						} else if (networkInfo.getSubtype() == telephonyManager.NETWORK_TYPE_LTE) {
							netType = NETTYPE_4g;
							break;
						}
				}
			}
		} else {
			netType = NETTYPE_NoCoonnected;
		}
		return netType;
	}

}
