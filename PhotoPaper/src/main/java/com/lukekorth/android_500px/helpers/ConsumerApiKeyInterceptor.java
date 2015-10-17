package com.lukekorth.android_500px.helpers;

import com.lukekorth.android_500px.BuildConfig;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class ConsumerApiKeyInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .url(chain.request()
                        .httpUrl()
                        .newBuilder()
                        .addQueryParameter("consumer_key", BuildConfig.CONSUMER_KEY)
                        .build())
                .build();
        return chain.proceed(request);
    }
}
