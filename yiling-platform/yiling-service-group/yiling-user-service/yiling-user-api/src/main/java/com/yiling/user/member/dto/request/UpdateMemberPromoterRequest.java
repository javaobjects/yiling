package com.yiling.user.member.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-更新会员推广人或推广方 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class UpdateMemberPromoterRequest extends BaseRequest {

    /**
     * 购买记录ID
     */
    private Long id;

    /**
     * 推广方ID
     */
    private Long promoterId;

    /**
     * 推广人手机号
     */
    private String mobile;

}
