package com.haoche51.checker.net;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 参数类型实现类
 * Created by wfx on 2016/10/17.
 */

public class ParameterizedTypeImpl implements ParameterizedType {
    private final Class raw;
    private final Type[] args;

    public ParameterizedTypeImpl(Class raw, Type[] args){
        this.raw=raw;
        this.args=args!=null?args:new Type[0];
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    @Override
    public Type getRawType() {
        return raw;
    }
}
