package com.yiling.dataflow.wash.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2023/5/11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SaveOrUpdateUnlockThirdRecordRequest extends BaseRequest {

    private static final long serialVersionUID = 3949595020094353016L;

    private Long id;

    private Long orgCrmId;

    private String customerName;

    private BigDecimal purchaseQuota;

    private List<DepartmentInfo> departmentInfoList;

    private String remark;

    @Data
    public static class DepartmentInfo implements Serializable {

        private static final long serialVersionUID = -2018129695469924727L;

        private String code;

        private String name;
    }
}
