package com.yht.iptv.callback;

import com.lzy.okgo.callback.AbsCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * ================================================
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * 修订历史：
 * -
 * -
 * -
 * -
 * -我的注释都已经写的不能再多了,不要再来问我怎么获取数据对象,怎么解析集合数据了,你只要会 gson ,就会解析
 * -
 * -
 * -
 * ================================================
 */
public abstract class JsonMallCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    public JsonMallCallback() {
    }

    public JsonMallCallback(Type type) {
        this.type = type;
    }

    public JsonMallCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {

        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonMallConvert<T> convert = new JsonMallConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }

        JsonMallConvert<T> convert = new JsonMallConvert<>(type);
        return convert.convertResponse(response);
    }
}