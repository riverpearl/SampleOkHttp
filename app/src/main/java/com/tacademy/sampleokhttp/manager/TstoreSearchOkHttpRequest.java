package com.tacademy.sampleokhttp.manager;

import com.google.gson.Gson;
import com.tacademy.sampleokhttp.autodata.Tstore;
import com.tacademy.sampleokhttp.autodata.TstoreResult;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Created by Tacademy on 2016-08-09.
 */
public class TstoreSearchOkHttpRequest extends OkHttpRequest<Tstore> {

    public static final String SORT_ACCURACY = "R";
    public static final String SORT_LATEST = "L";
    public static final String SORT_DOWNLOAD = "D";

    Request request;

    public TstoreSearchOkHttpRequest(String keyword) {
        this(keyword, 1, 10, SORT_LATEST);
    }

    public TstoreSearchOkHttpRequest(String keyword, int page, int count, String sort) {

        // Set url
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("apis.skplanetx.com")
                .addPathSegment("tstore")
                .addPathSegment("products")
                .addQueryParameter("version","1")
                .addQueryParameter("page","" + page)
                .addQueryParameter("count","" + count)
                .addQueryParameter("searchKeyword", keyword)
                .addQueryParameter("order",sort)
                .build();

        // Set header
        request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("appKey","b9982d77-eefc-30ce-8e7e-25a52cc10bb6")
                .build();
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    protected Tstore parse(ResponseBody body) {
        Gson gson = new Gson();
        TstoreResult result = gson.fromJson(body.charStream(), TstoreResult.class);
        return result.getTstore();
    }
}
