package com.yiling.sjms.gb.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 保存团购提报Request
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveGBInfoRequest extends BaseRequest {

    /**
     * 表单ID
     */
    private Long gbId;

    /**
     * 业务类型：1-提报 2-取消
     */
    private Integer bizType;

    /**
     * 省区名称
     */
    private String provinceName;

    /**
     * 事业部Id
     */
    private Long orgId;

    /**
     * 事业部名称
     */
    private String orgName;

    /**
     * 销量计入人工号
     */
    private String sellerEmpId;

    /**
     * 销量计入人姓名
     */
    private String sellerEmpName;

    /**
     * 销量计入人区办ID
     */
    private Long sellerDeptId;

    /**
     * 销量计入人区办名称
     */
    private String sellerDeptName;

    /**
     * 销量计入人部门名称
     */
    private String sellerYxDeptName;

    /**
     * 营销区办名称
     */
    private String bizArea;

    /**
     * 营销省区名称
     */
    private String bizProvince;

    /**
     * 团购负责人工号
     */
    private String managerEmpId;

    /**
     * 团购负责人姓名
     */
    private String managerEmpName;

    /**
     * 团购负责人部门名称
     */
    private String managerYxDeptName;

    /**
     * 团购单位ID
     */
    private Long customerId;

    /**
     * 团购单位名称
     */
    private String customerName;

    /**
     * 团购月份
     */
    private String monthTime;


    /**
     * 申请团购政策-返利金额（元）
     */
    private BigDecimal rebateAmount;

    /**
     * 团购区域：1-国内 2-海外
     */
    private Integer regionType;

    /**
     * 团购性质：1-普通团购 2-政府采购
     */
    private Integer gbType;

    /**
     * 团购证据-证据类型（多个以,分隔）
     */
    private String evidences;

    /**
     * 团购证据-其他
     */
    private String otherEvidence;

    /**
     * 备注
     */
    private String remark;

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
     * 发起审批时间
     */
    private Date submitTime;

    /**
     * 状态：10-待提交 20-审批中 200-已通过 201-已驳回
     */
    private Integer status;


    /**
     * 库终端和商业信息
     */
    private List<SaveGBCompanyRelationRequest> companyList;

    /**
     * 文件key
     */
    private List<FileInfoRequest> fileKeyList;

    /**
     * 类型 1-保存草稿 2-提交 3-驳回提交
     */
    private Integer type;
}
