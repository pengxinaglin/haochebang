package com.haoche51.checker.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.haoche51.checker.net.ParameterizedTypeImpl;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 将Json串与对象间相互转换
 * Created by wfx on 2016/10/17.
 */

public class JsonParseUtil {
    private static Gson mGson = new Gson();

    public static <T> T fromJsonObject(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        T obj = null;
        try {
            obj = mGson.fromJson(json, clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }


    public static <T> String toJsonObject(T obj) {
        if (obj==null) {
            return null;
        }
        String jsonStr = "";
        try {
            jsonStr = mGson.toJson(obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * 将Json数组转化成List<Object>
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        StringReader reader = new StringReader(json);
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        // 根据List<T>生成完整的Result<List<T>>
        List<T> obj=null;
        try {
            obj = mGson.fromJson(reader, listType);
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    public static <T> String toJsonArray(List<T> list) {
        if (list==null) {
            return null;
        }
        String jsonStr = "";
        try {
            jsonStr = mGson.toJson(list);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonStr;
    }

//    public static <T> String toJsonArray(List<T> list, Class<T> clazz) {
//        if (list==null) {
//            return null;
//        }
//        String jsonStr = "";
//        try {
//            Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
//            jsonStr = mGson.toJson(list, listType);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return jsonStr;
//    }
}
