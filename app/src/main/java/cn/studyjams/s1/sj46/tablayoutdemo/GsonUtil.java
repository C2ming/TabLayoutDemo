package cn.studyjams.s1.sj46.tablayoutdemo;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-22.
 */
public class GsonUtil {
    private static Gson gson = new Gson();

//    public static <T> List<T> formateListBean(String json, Class<T> c) {
//        try {
//            ResultList resultListBean = gson.fromJson(json, ResultList.class);
//            List<T> lst = new ArrayList<T>();
//            JsonArray array = new JsonParser().parse(resultListBean.getRows().toString()).getAsJsonArray();
//            for (final JsonElement elem : array) {
//                lst.add(gson.fromJson(elem, c));
//            }
//            return lst;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static <T> List<T> formateArray(String json, Type c) {
        try {
            List<T> lst = new ArrayList<T>();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                T t = gson.fromJson(elem, c);
                lst.add(t);
            }
            return lst;
        } catch (Exception e) {
            return null;
        }
    }
    public static <T> List<T> formateArray(String json, Class<T> c) {
        try {
            List<T> lst = new ArrayList<T>();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (final JsonElement elem : array) {
                T t = gson.fromJson(elem, c);
                lst.add(t);
            }
            return lst;
        } catch (Exception e) {
            return null;
        }
    }
    public static <T> T formateBean(String json, Class<T> c) {
        Result resultBean = gson.fromJson(json, Result.class);
        return gson.fromJson(resultBean.getData(), c);
    }

    public static Result formateResult(String json) {
        try {
            return gson.fromJson(json, Result.class);
        } catch (Exception e) {
            Log.e(e);
            return null;
        }
    }

    public static <T> T normalformate(String result, Class<T> c) {
        try {
            return gson.fromJson(result, c);
        } catch (Exception e) {
            Log.e(e);
            return null;
        }
    }

    public static <T> T normalformate(String result, Type type) {
        try {
            return gson.fromJson(result, type);
        } catch (Exception e) {
            Log.e(e);
            return null;
        }
    }
}
