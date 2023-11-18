package com.yiling.user.member.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.user.common.util.bean.Eq;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-查询会员购买条件 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-10-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMemberBuyStageRequest extends BaseRequest {

    /**
     * 会员ID
     */
    @Eq
    private Long memberId;

    /**
     * 价格
     */
    @Eq
    private BigDecimal price;

    /**
     * 有效时长
     */
    @Eq
    private Integer validTime;

    /**
     * 名称（如：季卡VIP）
     */
    @Eq
    private String name;

}
