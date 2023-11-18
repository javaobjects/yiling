package com.yiling.framework.rocketmq.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MqMsgConvertUtil {

    public static final byte[] EMPTY_BYTES = new byte[0];

    public MqMsgConvertUtil() {
    }

    public static byte[] objectSerialize(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        baos.close();
        return baos.toByteArray();
    }

    public static Serializable objectDeserialize(byte[] bytes)
        throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.close();
        bais.close();
        return (Serializable) ois.readObject();
    }

    public static final byte[] string2Bytes(String s, String charset) {
        if (null == s) {
            return EMPTY_BYTES;
        } else {
            byte[] bs = null;

            try {
                bs = s.getBytes(charset);
            } catch (Exception var4) {
                ;
            }

            return bs;
        }
    }

    public static final String bytes2String(byte[] bs, String charset) {
        if (null == bs) {
            return "";
        } else {
            String s = null;

            try {
                s = new String(bs, charset);
            } catch (Exception var4) {
                ;
            }

            return s;
        }
    }
}
