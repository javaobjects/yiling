package com.yiling.user.member.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员购买条件 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MemberBuyStageDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 有效时长
     */
    private Integer validTime;

    /**
     * 名称（如：季卡VIP）
     */
    private String name;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 排序
     */
    private Integer sort;

}
