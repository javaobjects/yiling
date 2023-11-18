package com.yiling.dataflow.config;

import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author: houjie.sun
 * @date: 2022/6/28
 */
@Getter
@Setter
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "tag")
public class FlowTagConfig {

    /**
     * 不计入药品配置
     */
    private List<Long> bujiru;

    public List<Long> getBujiruList() {
        if (CollUtil.isEmpty(bujiru)) {
            return ListUtil.empty();
        }
        return bujiru;
    }

    /**
     * 万州招商普药配置
     */
    private List<Long> wanzhou;

    public List<Long> getWanzhouList() {
        if (CollUtil.isEmpty(wanzhou)) {
            return ListUtil.empty();
        }
        return wanzhou;
    }

    /**
     * 招商普药配置
     */
    private List<Long> zhaoshang;

    public List<Long> getZhaoshangList() {
        if (CollUtil.isEmpty(zhaoshang)) {
            return ListUtil.empty();
        }
        return zhaoshang;
    }

    /**
     * 处理库存药品集合
     */
    private List<Long> kucui;

    public List<Long> getKucuiList() {
        if (CollUtil.isEmpty(kucui)) {
            return ListUtil.empty();
        }
        return kucui;
    }

    /**
     * 集采药品  code+"|"+"省份"
     */
    private List<String> jicai;

    public List<String> getJicaiList() {
        if (CollUtil.isEmpty(jicai)) {
            return ListUtil.empty();
        }
        return jicai;
    }

    /**
     * 中药饮片药品
     */
    private List<Long> zhongyao;

    public List<Long> getZhongyaoList() {
        if (CollUtil.isEmpty(zhongyao)) {
            return ListUtil.empty();
        }
        return zhongyao;
    }

    /**
     * 双花药品
     */
    private List<Long> shuanghua;

    public List<Long> getShuanghuaList() {
        if (CollUtil.isEmpty(shuanghua)) {
            return ListUtil.empty();
        }
        return shuanghua;
    }

    /**
     * 参灵蓝
     */
    private List<Long> canlinglan;

    public List<Long> getCanlinglanList() {
        if (CollUtil.isEmpty(canlinglan)) {
            return ListUtil.empty();
        }
        return canlinglan;
    }
}
