package com.yiling.user.member.dto.request;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-取消导入购买记录 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CancelBuyRecordRequest extends BaseRequest {

    /**
     * 购买记录ID
     */
    @NotNull
    private Long id;

}
