package com.yiling.admin.erp.redis.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JavaSerializer {

    public static byte[] serialize(Object obj) {
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)
                ) {
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Redis serialize exception, " + e.getMessage(), e);
        }
    }

    public static Object deserialize(byte[] data) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("Redis deserialize exception, " + e.getMessage(), e);
        }
    }

}
