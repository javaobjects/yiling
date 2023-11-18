package com.yiling.framework.common.encrypt;

import org.springframework.context.annotation.Bean;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;

public class EncrytablePropertyConfig {

    @Bean(name = "encryptablePropertyDetector")
    public EncryptablePropertyDetector encryptablePropertyDetector() {
        return new com.yiling.framework.common.encrypt.MyEncryptablePropertyDetector();
    }

    @Bean(name = "encryptablePropertyResolver")
    public EncryptablePropertyResolver encryptablePropertyResolver() {
        return new MyEncryptablePropertyResolver();
    }
}
