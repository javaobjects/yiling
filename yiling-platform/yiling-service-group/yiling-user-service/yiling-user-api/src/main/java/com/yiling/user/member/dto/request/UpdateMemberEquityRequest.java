package com.yiling.user.member.dto.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-修改权益 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateMemberEquityRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 权益名称
     */
    private String name;

    /**
     * 权益图标
     */
    private String icon;

    /**
     * 权益说明
     */
    private String description;

    /**
     * 权益状态：0-关闭 1-开启
     */
    private Integer status;


}
