package com.yiling.marketing.presale.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 营销活动主表
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PresaleRelativeDTO extends BaseDTO {

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 推广方id
     */
    private Long promoterEid;

    /**
     * 买家id
     */
    private Long buyerEid;
}
