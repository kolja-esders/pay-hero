package edu.pietro.team.payhero.helper;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpClient {

    private String mBaseUrl;

    private static AsyncHttpClient mClient = new AsyncHttpClient();

    public HttpClient(String baseUrl) {
        mBaseUrl = baseUrl;
    }

    public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        mClient.post(url, params, responseHandler);
    }

    private String getAbsoluteUrl(String relativeUrl) {
        return mBaseUrl + relativeUrl;
    }
}