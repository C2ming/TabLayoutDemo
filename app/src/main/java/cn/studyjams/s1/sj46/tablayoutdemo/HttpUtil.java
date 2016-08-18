package cn.studyjams.s1.sj46.tablayoutdemo;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * Created by zhy on 15/8/17.
 */
public class HttpUtil {
    private static HttpUtil mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private static boolean debug;

    private final int CONNECT_TIME_OUT = 15;

    @SuppressLint("NewApi")
    private HttpUtil() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.setConnectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS);
        // cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mDelivery = new Handler(Looper.getMainLooper());
        setDebug(false);
    }

    public static HttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    private Response _get(String url) throws IOException {
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        log(request);
        return execute;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return 字符串
     */
    private String _getAsString(String url) throws IOException {
        Response execute = _get(url);
        return execute.body().string();
    }

    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    private void _getAsyn(String url, final ResultCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        log(request);
        deliveryResult(callback, request);
    }

    private void _getAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        final Request request = new Request.Builder().url(sb.toString()).build();
        log(request);
        deliveryResult(callback, request);
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return
     */
    private Response _post(String url, Param... params) throws IOException {
        Request request = buildPostRequest(url, params);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param map post的参数
     * @return
     */
    private Response _post(String url, Map<String, String> map) throws IOException {
        Param params[] = map2Params(map);
        Request request = buildPostRequest(url, params);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return 字符串
     */
    private String _postAsString(String url, Param... params) throws IOException {
        Response response = _post(url, params);
        return response.body().string();
    }

    /**
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    private String _postAsString(String url, Map<String, String> params) throws IOException {
        Response response = _post(url, params);
        return response.body().string();
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Param... params) {
        Request request = buildPostRequest(url, params);
//        log(request, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }

    /**
     * 同步基于post的文件上传
     *
     * @param params
     * @return
     */
    private Response _post(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        return mOkHttpClient.newCall(request).execute();
    }

    private Response _post(String url, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 异步基于post的文件上传
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件不带参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, null);
        deliveryResult(callback, request);
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param params
     * @throws IOException
     */
    private void _postAsyn(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步下载文件
     *
     * @param url
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    private void _downloadAsyn(final String url, final String destFileDir, final ResultCallback callback) {
        final Request request = new Request.Builder().url(url).build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                sendFailedStringCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    // 如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(response, file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }

            }
        });
    }

    private String getFileName(String path) {
//        int separatorIndex = path.lastIndexOf("/");
//        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
        return EncoderUtils.encodeMd5(path);
    }

    private void setErrorResId(final ImageView view, final int errorResId) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                view.setImageResource(errorResId);
            }
        });
    }

    // *************对外公布的方法************

    /**
     * Function 同步Get
     *
     * @param url
     * @return
     * @throws IOException 2015-8-24 下午4:58:56 by lym
     */
    public static Response get(String url) throws IOException {
        return getInstance()._get(url);
    }

    /**
     * Function 同步Get
     *
     * @param url
     * @return String
     * @throws IOException 2015-8-24 下午4:59:04 by lym
     */
    public static String getAsString(String url) throws IOException {
        return getInstance()._getAsString(url);
    }

    /**
     * Function 异步Get
     *
     * @param url
     * @param callback 2015-8-24 下午4:59:24 by lym
     */
    public static void getAsyn(String url, ResultCallback callback) {
        getInstance()._getAsyn(url, callback);
    }

    /**
     * Function 异步Get
     *
     * @param url
     * @param callback 2015-8-24 下午4:59:24 by lym
     */
    public static void getAsyn(String url, ResultCallback callback, Map<String, String> params) {
        getInstance()._getAsyn(url, callback,params);
    }

    /**
     * Function 同步post
     *
     * @param url
     * @param params
     * @return
     * @throws IOException 2015-8-24 下午4:59:33 by lym
     */
    public static Response post(String url, Param... params) throws IOException {
        return getInstance()._post(url, params);
    }

    public static Response post(String url, Map<String, String> params) throws IOException {
        return getInstance()._post(url, params);
    }

    ;

    /**
     * Function 同步post
     *
     * @param url
     * @param params
     * @return String
     * @throws IOException 2015-8-24 下午4:59:42 by lym
     */
    public static String postAsString(String url, Param... params) throws IOException {
        return getInstance()._postAsString(url, params);
    }

    /**
     * Function 同步post
     *
     * @param url
     * @param params
     * @return String
     * @throws IOException 2015-8-24 下午4:59:42 by lym
     */
    public static String postAsString(String url, Map<String, String> params) throws IOException {
        return getInstance()._postAsString(url, params);
    }

    /**
     * Function 异步 post
     *
     * @param url
     * @param callback
     * @param params   2015-8-24 下午4:59:54 by lym
     */
    public static void postAsyn(String url, final ResultCallback callback, Param... params) {
        getInstance()._postAsyn(url, callback, params);
    }

    /**
     * Function 异步post
     *
     * @param url
     * @param callback
     * @param params   2015-8-24 下午5:00:07 by lym
     */
    public static void postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        getInstance()._postAsyn(url, callback, params);
    }

    /**
     * Function 同步上传文件
     *
     * @param url
     * @param files
     * @param fileKeys
     * @param params
     * @return
     * @throws IOException 2015-8-24 下午5:00:16 by lym
     */
    public static Response postFile(String url, File[] files, String[] fileKeys, Param... params) throws IOException {
        return getInstance()._post(url, files, fileKeys, params);
    }

    /**
     * Function 同步上传文件
     *
     * @param url
     * @param file
     * @param fileKey
     * @return
     * @throws IOException 2015-8-24 下午5:00:33 by lym
     */
    public static Response postFile(String url, File file, String fileKey) throws IOException {
        return getInstance()._post(url, file, fileKey);
    }

    /**
     * Function 同步上传文件
     *
     * @param url
     * @param file
     * @param fileKey
     * @param params
     * @return
     * @throws IOException 2015-8-24 下午5:00:51 by lym
     */
    public static Response postFile(String url, File file, String fileKey, Param... params) throws IOException {
        return getInstance()._post(url, file, fileKey, params);
    }

    /**
     * Function 异步上传文件
     *
     * @param url
     * @param callback
     * @param files
     * @param fileKeys
     * @param params
     * @throws IOException 2015-8-24 下午5:01:02 by lym
     */
    public static void postFileAsyn(String url, ResultCallback callback, File[] files, String[] fileKeys, Param... params) throws IOException {
        getInstance()._postAsyn(url, callback, files, fileKeys, params);
    }

    /**
     * Function 异步上传文件
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @throws IOException 2015-8-24 下午5:01:12 by lym
     */
    public static void postFileAsyn(String url, ResultCallback callback, File file, String fileKey) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey);
    }

    /**
     * Function 异步上传文件
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param params
     * @throws IOException 2015-8-24 下午5:01:21 by lym
     */
    public static void postFileAsyn(String url, ResultCallback callback, File file, String fileKey, Param... params) throws IOException {
        getInstance()._postAsyn(url, callback, file, fileKey, params);
    }

    /**
     * Function 异步下载文件
     *
     * @param url
     * @param destDir
     * @param callback 2015-8-24 下午5:01:31 by lym
     */
    public static void downloadAsyn(String url, String destDir, ResultCallback callback) {
        getInstance()._downloadAsyn(url, destDir, callback);
    }

    // ****************************

    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Param[] params) {
        params = validateParam(params);

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""), RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                // 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""), fileBody);

            }
        }
        log(url, fileKeys, params);
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else
            return params;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null)
            return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    public void deliveryResult(final ResultCallback callback, final Request request) {
        final Call call = mOkHttpClient.newCall(request);
        HttpCallManager.getInstance().addCall(request.urlString(), call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                HttpCallManager.getInstance().removeCall(request.urlString());
                sendFailedStringCallback(request, e, callback);

            }

            @Override
            public void onResponse(final Response response) {
                HttpCallManager.getInstance().removeCall(request.urlString());
                try {
                    final String string = response.body().string();
                    sendSuccessResultCallback(response, string, callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }

            }
        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (isDebug()) {
                    log(request);
                }
                if (callback != null) {
                    callback.onError(request, e);
                    callback.onFinish();
                }
            }
        });
    }

    private void sendSuccessResultCallback(final Response response, final String responseString, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (isDebug()) {
                    Log.d(responseString);
                }
                if (callback != null) {
                    callback.onResponse(response, responseString);
                    callback.onFinish();
                }
            }
        });
    }

    private Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }

        log(url, params);
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    public static boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        HttpUtil.debug = debug;
    }

    public static abstract class ResultCallback {

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(Response response, String responseString);

        public void onFinish() {
        }

        ;
    }

    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

    private static void log(Request request, Param... params) {
        if (isDebug()) {
            StringBuilder builder = new StringBuilder();
            builder.append(request.urlString());
            for (Param param : params) {
                builder.append(param.key).append("=").append(param.value).append("&");
            }
            if (params.length > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            Log.d(builder.toString());
        }
    }

    private static void log(String url, String[] fileKeys, Param... params) {
        if (isDebug()) {
            StringBuilder log = new StringBuilder();
            log.append(url);
            for (Param param : params) {
                log.append(param.key).append("=").append(param.value).append("&");
            }
            if (params.length > 0) {
                log.deleteCharAt(log.length() - 1);
            }
            log.append("\n").append("fileKey:");
            for (String key : fileKeys) {
                log.append(key).append(",");
            }
            if (fileKeys.length > 0)
                log.deleteCharAt(log.length() - 1);
            Log.d(log.toString());
        }
    }

    private static void log(String url, Param... params) {
        if (isDebug()) {
            StringBuilder builder = new StringBuilder();
            builder.append(url);
            for (Param param : params) {
                builder.append(param.key).append("=").append(param.value).append("&");
            }
            if (params.length > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            Log.d(builder.toString());
        }
    }
}