package com.yiling.user.control.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2021/10/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveRegionControlRequest extends BaseRequest {

    private Integer customerTypeSet;

    private List<Long> customerTypes;

    private Integer regionSet;

    private List<Long> regionIds;

    private String controlDescribe;

    private Long goodsId;

    private Long eid;
}

