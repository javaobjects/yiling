package com.yiling.framework.common.encrypt;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;

public class MyEncryptablePropertyDetector implements EncryptablePropertyDetector {

    /**
     * 加密内容需要以这个内容开头，如：ENC@tWaTyvKPzdScShFzRDs
     */
    public static final String ENCODED_PASSWORD_HINT = "ENC@";

    /**
     * 如果属性的字符开头为 ENCODED_PASSWORD_HINT，返回true，表明该属性是加密过的
     * @param property
     * @return
     */
    @Override
    public boolean isEncrypted(String property) {
        if (null != property) {
            return property.startsWith(ENCODED_PASSWORD_HINT);
        }
        return false;
    }

    /**
     * 该方法告诉工具，如何将自定义前缀去除
     * @param property
     * @return
     */
    @Override
    public String unwrapEncryptedValue(String property) {
        return property.substring(ENCODED_PASSWORD_HINT.length());
    }
}