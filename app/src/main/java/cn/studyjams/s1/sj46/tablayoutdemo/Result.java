package cn.studyjams.s1.sj46.tablayoutdemo;

import com.google.gson.JsonObject;

/**
 * Created by Administrator on 2016-07-22.
 */
public class Result {


    private int errorCode;
    private boolean success;
    private String msg;
    private JsonObject data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
