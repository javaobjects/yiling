package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveHospitalDrugstoreRelRequest extends BaseRequest {

    private static final long serialVersionUID = 1496596084870155621L;

    private Long id;
}
