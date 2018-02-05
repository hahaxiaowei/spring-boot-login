package com.huntkey.rx.sceo.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by lulx on 2017/6/23 0023.
 */
public class SerializeUtils {

    /**
     * @return java.lang.Object
     * @desc 反序列化
     * @pars [bytes]
     * @date 2017/6/23 0023 下午 2:41 lulx
     **/
    public static Object deserialize(byte[] bytes) {
        try {
            ByteArrayInputStream byteInt = new ByteArrayInputStream(bytes);
            ObjectInputStream objInt = new ObjectInputStream(byteInt);
            return objInt.readObject();
        } catch (Exception e) {
            throw new RuntimeException("反序列化错误", e);
        }
    }

    /**
     * @return byte[]
     * @desc 序列化
     * @pars [o]
     * @date 2017/6/23 0023 下午 2:42 lulx
     **/
    public static byte[] serialize(Object o) {
        try {
            ByteArrayOutputStream byt = new ByteArrayOutputStream();
            ObjectOutputStream obj = new ObjectOutputStream(byt);
            obj.writeObject(o);
            return byt.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("序列化错误", e);
        }
    }
}
