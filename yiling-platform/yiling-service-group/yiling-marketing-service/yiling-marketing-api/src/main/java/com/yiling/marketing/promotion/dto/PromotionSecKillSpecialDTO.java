package com.yiling.marketing.promotion.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 促销活动-秒杀&特价表
 * </p>
 *
 * @author: fan.shen
 * @date: 2022/1/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionSecKillSpecialDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 终端身份 1-会员，2-非会员，3-全部
     */
    private Integer terminalType;

    /**
     * 允许购买区域 1-全部，2-部分
     */
    private Integer permittedAreaType;

    /**
     * 允许购买区域明细json
     */
    private String permittedAreaDetail;

    /**
     * 企业类型 1-全部，2-部分
     */
    private Integer permittedEnterpriseType;

    /**
     * 企业类型json
     */
    private String permittedEnterpriseDetail;

}
