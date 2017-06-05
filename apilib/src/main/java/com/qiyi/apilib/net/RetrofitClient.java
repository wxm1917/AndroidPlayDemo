package com.qiyi.apilib.net;


import com.qiyi.apilib.utils.LogUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhouxiaming on 2017/5/5.
 */
public class RetrofitClient {
    private String TAG = RetrofitClient.class.getSimpleName();
    private String host;
    private static final int RETRY_COUNT = 1;
    private static final int RETRY_WAIT_TIME = 2000;
    private boolean isDebug = false;
    private boolean isRetry = false;
    private boolean isNeedSaveCookie = false;
    private Retrofit retrofit;

    /**
     *
     * @param host 域名
     * @param isDebug 是否需要打印LOG
     * @param retry 重试次数
     * @param needSaveCookie 是否需要解析并保存Cookie信息
     */
    public RetrofitClient(String host, boolean isDebug, boolean retry, boolean needSaveCookie) {
        this.host = host;
        this.isDebug = isDebug;
        this.isRetry = retry;
        this.isNeedSaveCookie = needSaveCookie;
        createRetrofit(createHttpClient());
    }


    private void createRetrofit(OkHttpClient httpclient) {
        retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .client(httpclient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public <T> T createApi(Class<T> apiClass) {
        return retrofit.create(apiClass);
    }

    private OkHttpClient createHttpClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        //拦截器:打印Log
        if (isDebug) {
            clientBuilder.addInterceptor(new LogInterceptor());
        }

        //拦截器:重试请求
        if (isRetry) {
            clientBuilder.addInterceptor(new RetryInterceptor());
        }

        //拦截器:处理Cookie信息
        if (isNeedSaveCookie) {
            clientBuilder.addInterceptor(new CookieInterceptor());
        }

        //Https证书验证
        if (host != null && (host.startsWith("https") || host.startsWith("HTTPS"))) {
            supportHttps(clientBuilder);
        }

        return clientBuilder.build();
    }

    /**
     * 忽略Https验证
     *
     * @param builder
     */
    private void supportHttps(OkHttpClient.Builder builder) {
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            builder.sslSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析并缓存返回的cookie信息
     */
    class CookieInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            if (response != null) {
                //CookieManager.updateCookieCache(response.headers());
            }
            return response;
        }
    }

    /**
     * 重试拦截器
     */
    class RetryInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            // 当前在网络请求的子线程中

            LogUtils.i(TAG, "intercept: getId " + Thread.currentThread().getId());
            Response response = null;
            int retryCount = 0;
            boolean retry = true;

            while (retry && retryCount++ < RETRY_COUNT) {
                try {
                    response = chain.proceed(request);

                    // 没有产生Exception
                    retry = shouldRetry(response, null);
                } catch (ConnectException e) {
                    LogUtils.e(TAG, "ConnectException : " + e);
                    retry = shouldRetry(response, e);
                    if (!retry) {
                        throw e;
                    }
                } catch (SocketException e) {
                    LogUtils.e(TAG, "SocketException : " + e);
                    retry = shouldRetry(response, e);
                    if (!retry) {
                        throw e;
                    }
                } catch (UnknownHostException e) {
                    LogUtils.e(TAG, "UnknownHostException : " + e);
                    retry = shouldRetry(response, e);
                    if (!retry) {
                        throw e;
                    }
                } catch (SocketTimeoutException e) {
                    LogUtils.e(TAG, "SocketTimeoutException : " + e);
                    retry = shouldRetry(response, e);
                    if (!retry) {
                        throw e;
                    }
                } catch (Exception e) {
                    LogUtils.e(TAG, "Exception : " + e);
                    retry = shouldRetry(response, e);
                    if (!retry) {
                        throw e;
                    }
                }

                if (retry) {
                    LogUtils.e(TAG, "RetryInterceptor tryCount: " + retryCount);
                    try {
                        Thread.sleep(Math.min(retryCount * RETRY_WAIT_TIME, 1000 * 60));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (response == null) {//important ,should throw an exception here
                throw new IOException();
            }
            return response;
        }
    }

    // 默认网络不可用的情况下不会重试，如果需要重试的话需重载该函数。
    protected static boolean shouldRetry(Response response, Exception e) {
        if (response != null && response.isSuccessful()) {
            return false;
        }
        return true;
    }

    /**
     * 打印Log
     */
    class LogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            LogUtils.i(TAG, String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            if (response != null) {
                long t2 = System.nanoTime();
                LogUtils.i(TAG, String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            }
            return response;
        }
    }
}
