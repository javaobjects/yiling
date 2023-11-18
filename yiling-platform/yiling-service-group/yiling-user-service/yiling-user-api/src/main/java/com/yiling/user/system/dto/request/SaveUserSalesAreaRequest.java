package com.yiling.user.system.dto.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.basic.location.dto.LocationTreeDTO;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存用户销售区域设置 Request
 *
 * @author: xuan.zhou
 * @date: 2021/9/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveUserSalesAreaRequest extends BaseRequest {

    /**
     * 用户ID
     */
    @NotNull
    private Long userId;


    /**
     * 销售区域，如果不传则为全国
     */
    private List<LocationTreeDTO> salesAreaTree;

}
