package com.yiling.user.enterprise.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 统计可采购销售渠道商的个数 VO
 *
 * @author: yuecheng.chen
 * @date: 2021/6/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CountSellerChannelDTO extends BaseDTO {

    /**
     * 渠道商采购销购销售渠道商总个数
     */
    private Integer total;

    /**
     * 工业个数
     */
    private Integer industryCount;

    /**
     * 工业直属
     */
    private Integer industryDirectCount;

    /**
     * 一级商个数
     */
    private Integer level1Count;

    /**
     * 二级商个数
     */
    private Integer level2Count;

    /**
     * 专二普一个数
     */
    private Integer z2p1Count;
}
