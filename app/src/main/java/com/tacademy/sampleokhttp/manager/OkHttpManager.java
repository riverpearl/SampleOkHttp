package com.tacademy.sampleokhttp.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.tacademy.sampleokhttp.MyApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by Tacademy on 2016-08-09.
 */
public class OkHttpManager {

    // Singleton Pattern
    private static OkHttpManager instance;

    public static OkHttpManager getInstance() {
        if (instance == null)
            instance = new OkHttpManager();

        return instance;
    }

    OkHttpClient client;

    private OkHttpManager() {
        // 클라이언트를 설정해줄 builder 만들기
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // cookie 처리를 위한 context는 애플리케이션의 context를 얻어온다.
        Context context = MyApplication.getContext();

        // cookie를 처리하기 위해서 cookieStore을 구현해준다. (여기선 라이브러리 사용)
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        builder.cookieJar(cookieJar);

        // cache 처리 (유효하지 않은 dir이면 make dir)
        File cacheDir = new File(context.getCacheDir(), "network");
        if (!cacheDir.exists()) cacheDir.mkdir();
        Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);
        builder.cache(cache);

        // timeout 설정
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);

        // client에 setting
        client = builder.build();
    }

    private static final int MESSAGE_SUCCESS = 1;
    private static final int MESSAGE_FAIL = 2;

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            OkHttpRequest<?> request = (OkHttpRequest<?>) msg.obj;
            switch (msg.what) {
                case MESSAGE_SUCCESS:
                    request.sendSuccess();
                    break;
                case MESSAGE_FAIL:
                    request.sendFail();
                    break;
            }
        }
    };

    public interface OnResultListener<T> {
        public void onSuccess(OkHttpRequest<T> request, T result);

        public void onFail(OkHttpRequest<T> request, int errorCode, String errorMessage, Throwable e);
    }

    void sendSuccess(OkHttpRequest<?> request) {
        Message msg = mHandler.obtainMessage(MESSAGE_SUCCESS, request);
        mHandler.sendMessage(msg);
    }

    void sendFail(OkHttpRequest<?> request) {
        Message msg = mHandler.obtainMessage(MESSAGE_FAIL, request);
        mHandler.sendMessage(msg);
    }

    public <T> void getNetworkData(OkHttpRequest<T> request, OnResultListener<T> listener) {
        request.setOnResultListener(listener);
        request.process(client);
    }
}
