package com.yiling.user.member.dto.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-会员购买条件 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateMemberBuyStageRequest extends BaseRequest {

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
