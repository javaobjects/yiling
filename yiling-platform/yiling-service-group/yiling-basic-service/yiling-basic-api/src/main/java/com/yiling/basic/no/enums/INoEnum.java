package com.yiling.basic.no.enums;

/**
 * 业务单号接口
 *
 * @author: xuan.zhou
 * @date: 2023/2/27
 */
public interface INoEnum {

    /**
     * 名称
     */
    String name();

    /**
     * 单据号前缀
     */
    String getPrefix();

    /**
     * 中间部分生成方式
     */
    MiddelPartMode getMiddelPartMode();

    /**
     * 随机数位数
     */
    Integer getRandomNum();
}
