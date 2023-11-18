package com.yiling.sjms.gb.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;


/**
 * 保存团购信息
 */
@Data
public class SaveGBCancelInfoRequest extends BaseRequest {

    /**
     * 表单Id
     */
    private Long gbId;

    /**
     * 业务类型：1-提报 2-取消
     */
    private Integer bizType;

    /**
     *取消原因
     */
    private String cancelReason;

    /**
     * 发起人ID
     */
    private String empId;

    /**
     * 发起人姓名
     */
    private String empName;

    /**
     * 发起人部门ID
     */
    private Long deptId;

    /**
     * 发起人部门名称
     */
    private String deptName;

    /**
     * 营销区办名称
     */
    private String bizArea;

    /**
     * 营销省区名称
     */
    private String bizProvince;


    /**
     *类型 类型 2-提交 3-驳回提交
     */
    private Integer type;

    /**
     * 单据类型，参见FormTypeEnum枚举
     */
    private Integer formType;

    /**
     *团购费用申请原因
     */
    private String feeApplicationReply;

    /**
     *团购费用申请文件
     */
    private List<SaveFeeApplicationInfoRequest> attachInfoForms;
}
