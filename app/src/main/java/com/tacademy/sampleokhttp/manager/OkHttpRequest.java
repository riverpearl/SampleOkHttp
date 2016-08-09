package com.tacademy.sampleokhttp.manager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Tacademy on 2016-08-09.
 */
public abstract class OkHttpRequest<T> implements Callback {

    // request 보내기 메소드와 response parse 해주기 메소드
    // 상속받은 클래스에서 구현해준다.
    public abstract Request getRequest();
    protected abstract T parse(ResponseBody body);

    OkHttpManager.OnResultListener<T> rListener;
    public void setOnResultListener(OkHttpManager.OnResultListener<T> listener) {
        rListener = listener;
    }

    // request 실행 메소드 (비동기)
    Call call;
    void process(OkHttpClient client) {
        Request request = getRequest();
        call = client.newCall(request);
        call.enqueue(this); // 라이브러리를 보면 enqueue에서 parsing 해주도록 돼있음
    }

    // request 실행 메소드 (동기)
    public T processSync(OkHttpClient client) throws IOException {
        Request request = getRequest();
        call = client.newCall(request);
        Response response = call.execute();

        if (response.isSuccessful()) {
            T result = parse(response.body());
            return result;
        } else {
            throw new IOException("code : " + response.code() + ", message : " + response.message());
        }
    }

    T result;

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful())
            sendSuccess(parse(response.body()));
        else sendError(response.code(), response.message(), null);
    }

    private void sendSuccess(T result) {
        this.result = result;
        OkHttpManager.getInstance().sendSuccess(this);
    }

    void sendSuccess() {
        if (rListener != null)
            rListener.onSuccess(this, result);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        sendError(-1, e.getMessage(), e);
    }

    int code;
    String errorMessage;
    Throwable exception;

    protected  void sendError(int code, String errorMessage, Throwable exception) {
        this.code = code;
        this.errorMessage = errorMessage;
        this.exception = exception;
        OkHttpManager.getInstance().sendFail(this);
    }

    void sendFail() {
        if (rListener != null)
            rListener.onFail(this, code, errorMessage, exception);
    }
}
