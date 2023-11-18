package com.yiling.hmc.welfare.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author: fan.shen
 * @data: 2022/09/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveGroupRequest extends BaseRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 福利计划id
     */
    private Long welfareId;

    /**
     * 用药人姓名
     */
    private String medicineUserName;

    /**
     * 用药人手机号
     */
    private String medicineUserPhone;

    /**
     * 药店id
     */
    private Long eid;

    /**
     * 销售人员id
     */
    private Long sellerUserId;
}
