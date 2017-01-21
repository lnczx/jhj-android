package com.meijialife.dingdang.utils;


import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

/**
 * 网络请求
 */
public class AgentApi {

    //    private static String DEFAULT_USERAGENT = System.getProperty("http.agent") + " lesports";
//    private static String DEFAULT_USERAGENT = "user_id="+UserCenter.getInstance(App.mInstance).getId();
    private static String DEVICE_INFO = "";

    public static Callback.Cancelable get(String url, Callback.CommonCallback commonCallback) {
        return get(url, null, commonCallback);
    }

    public static Callback.Cancelable get(String url,
                                          HashMap<String, String> postParameters, Callback.CommonCallback commonCallback) {
        return request(HttpMethod.GET, url, postParameters, commonCallback);
    }

    public static Callback.Cancelable post(String url,
                                           HashMap<String, String> postParameters, Callback.CommonCallback commonCallback) {
        return post(url, postParameters, null, commonCallback);
    }

    public static Callback.Cancelable post(String url,
                                           HashMap<String, String> postParameters, String bodyContent, Callback.CommonCallback commonCallback) {
        return request(HttpMethod.POST, url, postParameters, bodyContent, commonCallback);
    }

    public static Callback.Cancelable request(HttpMethod method, String url,
                                              HashMap<String, String> postParameters, Callback.CommonCallback commonCallback) {
        return request(method, url, postParameters, null, commonCallback);
    }

    public static Callback.Cancelable put(String url,
                                          HashMap<String, String> postParameters, String bodyContent, Callback.CommonCallback commonCallback) {
        return request(HttpMethod.PUT, url, postParameters, bodyContent, commonCallback);

    }

    public static Callback.Cancelable upload(String url,
                                             HashMap<String, String> postParameters, String title,
                                             HashMap<String, File> files, String contentType
            , Callback.CommonCallback commonCallback) {
        RequestParams requestParams = new RequestParams(url);
//        requestParams.addHeader("X-Remote-User-Info", DEFAULT_USERAGENT);
//        requestParams.addHeader("X-Remote-Client-Info", DEVICE_INFO);
        requestParams.setAsJsonContent(true);
        requestParams.setMultipart(true);
        requestParams.setCharset("UTF-8");
        if (postParameters != null) {
            Set<String> set = postParameters.keySet();
            for (String key : set) {
                requestParams.addParameter(key, postParameters
                        .get(key));
            }
        }

        if (files != null) {
            Set<String> set = files.keySet();
            for (String name : set) {
                requestParams.addBodyParameter(title, files.get(name), contentType, name);
            }
        }


        return x.http().request(HttpMethod.POST, requestParams, commonCallback);
    }

    private static Callback.Cancelable request(HttpMethod method, String url,
                                               HashMap<String, String> postParameters, String bodyContent, Callback.CommonCallback commonCallback) {
        RequestParams requestParams = new RequestParams(url);

        requestParams.addHeader("X-Remote-Client-Info", DEVICE_INFO);
        requestParams.addHeader("Content-Type", "application/json");
        if (null != postParameters) {
            Set<String> set = postParameters.keySet();
            for (String key : set) {
                requestParams.addBodyParameter(key, postParameters
                        .get(key));
            }
        }

        if (null != bodyContent) {
            requestParams.setAsJsonContent(true);
            requestParams.setCharset("UTF-8");
            requestParams.setBodyContent(bodyContent);
        }

//        LogUtil.R("HttpRequest Header 1= " + requestParams.getHeaders().get(0).getValueStr());
//        LogUtil.R("HttpRequest Header 2= " + requestParams.getHeaders().get(1).getValueStr());
//        LogUtil.R("HttpRequest Header 3= " + requestParams.getHeaders().get(2).getValueStr());
        LogOut.i("AgentApi", "url::" + requestParams.toString());

        return x.http().request(method, requestParams, commonCallback);
    }

    public static Callback.Cancelable request(String url,
                                              RequestParams requestParams, Callback.CommonCallback commonCallback) {
        requestParams.addHeader("X-Remote-Client-Info", DEVICE_INFO);

        LogOut.i("AgentApi", "url::" + url);
//        LogUtil.R("HttpRequest Header 1= " + requestParams.getHeaders().get(0).getValueStr());
//        LogUtil.R("HttpRequest Header 2= " + requestParams.getHeaders().get(1).getValueStr());
//        LogUtil.R("HttpRequest Header 3= " + requestParams.getHeaders().get(2).getValueStr());

        return x.http().request(HttpMethod.GET, requestParams, commonCallback);
    }

    public static void setDeviceInfo(String deviceInfo) {
//        LogUtil.R("X-Remote-Client-Info = " + deviceInfo);
        DEVICE_INFO = deviceInfo;
    }

}