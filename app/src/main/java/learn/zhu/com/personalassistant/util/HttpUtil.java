package learn.zhu.com.personalassistant.util;

import android.database.sqlite.SQLiteDatabase;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhu on 2017/5/1.
 */

public class HttpUtil {
    public static final String HOST = "http://10.0.2.2/api/";
    /**
     * 发送http请求
     * @param url 请求链接
     * @param callback 回调函数
     */
    public static void sendOkHttpRequest(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * 发送POST请求
     * @param url 请求链接
     * @param requestBody post参数
     * @param callback 回调函数
     */
    public static void sendOkHttpRequest(String url, RequestBody requestBody, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static String getUrl(String arg) {
        return HOST + arg;
    }
}
