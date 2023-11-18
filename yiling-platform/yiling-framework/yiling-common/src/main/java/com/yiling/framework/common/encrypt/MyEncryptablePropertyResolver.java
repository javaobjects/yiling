package com.yiling.framework.common.encrypt;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;

public class MyEncryptablePropertyResolver implements EncryptablePropertyResolver {

    /**
     * 自定义解密方法
     * @param value
     * @return
     */
    @Override
    public String resolvePropertyValue(String value) {
        if (null != value && value.startsWith(com.yiling.framework.common.encrypt.MyEncryptablePropertyDetector.ENCODED_PASSWORD_HINT)) {
            return EncryptUtils.decrypt(value.substring(com.yiling.framework.common.encrypt.MyEncryptablePropertyDetector.ENCODED_PASSWORD_HINT.length()));
        }
        return value;
    }
}