package com.yiling.dataflow.wash.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveUnlockSaleCustomerRangeProvinceRequest extends BaseRequest {
    private Long ruleId;

    private Long uscId;

    private List<String> provinceCodeList;

}
