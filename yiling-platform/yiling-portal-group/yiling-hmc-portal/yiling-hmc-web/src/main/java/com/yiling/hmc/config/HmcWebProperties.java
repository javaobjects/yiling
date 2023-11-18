package com.yiling.hmc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * H5域名链接配置类
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/5/6
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "meds-science")
public class HmcWebProperties {

    /**
     * 标准库id列表
     */
    private List<Long> standardIdList;

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 商品id
     */
    private Long goodsId;
}
