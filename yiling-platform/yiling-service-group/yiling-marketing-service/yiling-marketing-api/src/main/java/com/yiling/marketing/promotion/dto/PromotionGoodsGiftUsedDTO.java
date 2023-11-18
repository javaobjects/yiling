package com.yiling.marketing.promotion.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/11/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PromotionGoodsGiftUsedDTO extends BaseDTO {

    /**
     * 赠品名称
     */
    private String giftName;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 员工姓名
     */
    private String buyerName;

    /**
     * 创建人id
     */
    private Long createUser;

    /**
     * 买家企业id
     */
    private Long buyerEid;

    /**
     * 员工电话
     */
    private String buyerTel;

    /**
     * 企业地址
     */
    private String address;
}
