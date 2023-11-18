package com.yiling.hmc.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * 同步商品配置
 *
 * @author: fan.shen
 * @date: 2022/6/16
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "sync.goods")
@RefreshScope
public class SyncGoodsConfig {

    /**
     * 西药
     */
    private Long westernMedicine;

    /**
     * 中成药
     */
    private Long chinesePatentMedicine;

    /**
     * 中药饮片
     */
    private Long chineseHerbalDecoctionPiece;

    /**
     * 配方颗粒
     */
    private Long dispensingGranules;

    /**
     * 院内制剂
     */
    private Long intrahospitalPreparations;
}
