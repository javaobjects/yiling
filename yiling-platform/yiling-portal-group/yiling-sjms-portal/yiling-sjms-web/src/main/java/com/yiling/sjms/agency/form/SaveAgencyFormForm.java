package com.yiling.sjms.agency.form;

import java.util.List;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 机构新增修改表单请求参数
 *
 * @author: yong.zhang
 * @date: 2023/2/22 0022
 */
@Data
public class SaveAgencyFormForm extends BaseForm {


    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 主流程表单id
     */
    @ApiModelProperty("主流程表单id")
    private Long formId;

    /**
     * 机构基本信息id
     */
    @ApiModelProperty("机构基本信息id")
    private Long crmEnterpriseId;

    /**
     * 供应链角色：1-商业公司 2-医疗机构 3-零售机构
     */
    @ApiModelProperty("供应链角色：1-商业公司 2-医疗机构 3-零售机构")
    private Integer supplyChainRole;
    /**
     * 机构名称
     */
    @ApiModelProperty("机构名称")
    private String name;
    /**
     * 社会信用统一代码
     */
    @ApiModelProperty("社会信用统一代码")
    private String licenseNumber;
    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;
    /**
     * 所属省份名称
     */
    @ApiModelProperty("所属省份名称")
    private String provinceName;
    /**
     * 所属城市名称
     */
    @ApiModelProperty("所属城市名称")
    private String cityName;
    /**
     * 所属区区域名称
     */
    @ApiModelProperty("所属区区域名称")
    private String regionName;
    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;
    /**
     * 所属省份编码
     */
    @ApiModelProperty("所属省份编码")
    private String provinceCode;
    /**
     * 所属城市编码
     */
    @ApiModelProperty("所属城市编码")
    private String cityCode;
    /**
     * 所属区域编码
     */
    @ApiModelProperty("所属区域编码")
    private String regionCode;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String notes;

    /**
     * 变更项目 机构名称、社会统一信用代码、电话、所属区域、地址
     */
    @ApiModelProperty("变更项目 机构名称、社会统一信用代码、电话、所属区域、地址")
    private String changeItem;

    /**
     * 变更项目 机构名称、社会统一信用代码、电话、所属区域、地址
     */
    @ApiModelProperty("变更项目 机构名称、社会统一信用代码、电话、所属区域、地址")
    private List<Integer> changeItemList;


    //    // =====================================
    //    /**
    //     * 发起人ID
    //     */
    //    @ApiModelProperty("发起人ID")
    //    private Long empId;
    //    /**
    //     * 发起人姓名
    //     */
    //    @ApiModelProperty("发起人姓名")
    //    private String empName;
    //    /**
    //     * 发起人部门ID
    //     */
    //    @ApiModelProperty("发起人部门ID")
    //    private Long deptId;
    //    /**
    //     * 发起人部门名称
    //     */
    //    @ApiModelProperty("发起人部门名称")
    //    private String deptName;
}
