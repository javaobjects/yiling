package com.yiling.sjms.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DeleteHospitalDrugstoreRelFormRequest extends BaseRequest {

    private static final long serialVersionUID = -5000822632022192305L;
    /**
     * 机构新增修改表 id
     */
    private Long id;
}
