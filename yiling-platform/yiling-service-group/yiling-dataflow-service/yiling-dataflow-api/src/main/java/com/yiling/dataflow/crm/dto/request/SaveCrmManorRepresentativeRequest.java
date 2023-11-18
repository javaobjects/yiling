package com.yiling.dataflow.crm.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveCrmManorRepresentativeRequest extends BaseRequest {
    private Long id;

    private Long manorId;
    /**
     * 代表岗位编码
     */
    private Long representativePostCode;

    /**
     * 代表岗位名称
     */
    private String representativePostName;
}
