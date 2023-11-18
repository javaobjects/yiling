package com.yiling.user.integral.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 修改快递信息立即兑付 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateExpressRequest extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 快递公司（见字典）
     */
    private String expressCompany;

    /**
     * 快递单号
     */
    private String expressOrderNo;

}