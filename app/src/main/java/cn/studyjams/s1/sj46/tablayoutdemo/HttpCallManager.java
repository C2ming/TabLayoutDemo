package cn.studyjams.s1.sj46.tablayoutdemo;

import android.text.TextUtils;

import com.squareup.okhttp.Call;

import java.util.concurrent.ConcurrentHashMap;


public class HttpCallManager {

    private ConcurrentHashMap<String, Call> callMap;
    private static HttpCallManager manager;

    private HttpCallManager() {
        callMap = new ConcurrentHashMap<>();
    }

    public static HttpCallManager getInstance() {
        if (manager == null) {
            manager = new HttpCallManager();
        }
        return manager;
    }

    public void addCall(String url, Call call) {
        if (call != null && !TextUtils.isEmpty(url)) {
            callMap.put(url, call);
        }
    }

    public void cancle(String url) {
        Call call = getCall(url);
        if (call != null) {
            call.cancel();
        }
    }

    public Call getCall(String url) {
        if (!TextUtils.isEmpty(url)) {
            return callMap.get(url);
        }

        return null;
    }

    public void removeCall(String url) {
        if (!TextUtils.isEmpty(url)) {
            callMap.remove(url);
        }
    }

}
