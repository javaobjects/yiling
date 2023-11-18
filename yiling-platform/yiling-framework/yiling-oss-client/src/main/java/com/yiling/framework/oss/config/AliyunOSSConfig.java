package com.yiling.framework.oss.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.Protocol;

/**
 * 阿里云配置
 */
@Configuration
public class AliyunOSSConfig {

    @Value("${aliyun.oss.access-key:xxxxx}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret:xxxxx}")
    private String accessKeySecret;
    @Value("${aliyun.oss.endpoint:xxxxx}")
    private String endpoint;
    @Value("${aliyun.oss.protocol:http}")
    private String protocol;

    /**
     * 阿里云文件存储client
     * 只有配置了aliyun.oss.access-key才可以使用
     */
    @Bean
    @ConditionalOnProperty(name = "aliyun.oss.access-key", matchIfMissing = true)
    public OSSClient ossClient() {
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);

        ClientConfiguration clientConfiguration = new ClientBuilderConfiguration();
        if (Protocol.HTTPS.toString().equalsIgnoreCase(protocol)) {
            clientConfiguration.setProtocol(Protocol.HTTPS);
        }

        OSSClient ossClient = new OSSClient(endpoint, credentialsProvider, clientConfiguration);
        return ossClient;
    }
}
