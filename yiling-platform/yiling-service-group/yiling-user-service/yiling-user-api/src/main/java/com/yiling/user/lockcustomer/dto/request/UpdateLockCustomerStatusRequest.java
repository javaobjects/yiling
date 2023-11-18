package com.yiling.user.lockcustomer.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 更新销售助手-锁定用户状态 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateLockCustomerStatusRequest extends BaseRequest {

    /**
     * ID集合
     */
    private List<Long> idList;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

}
