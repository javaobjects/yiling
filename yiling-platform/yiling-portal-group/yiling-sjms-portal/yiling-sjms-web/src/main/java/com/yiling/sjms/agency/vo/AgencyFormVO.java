package com.yiling.sjms.agency.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 机构新增修改表单
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-22
 */
@Data
public class AgencyFormVO extends BaseVO {

    /**
     * 主流程表单id
     */
    @ApiModelProperty("主流程表单id")
    private Long formId;

    /**
     * 机构基本信息id
     */
    private Long crmEnterpriseId;

    /**
     * crm系统对应客户名称
     */
    @ApiModelProperty("crm系统对应客户名称")
    private String name;

    /**
     * 社会信用统一代码
     */
    @ApiModelProperty("社会信用统一代码")
    private String licenseNumber;

    /**
     * 修改之前的crm系统对应客户名称
     */
    private String beforeName;

    /**
     * 修改之前的社会信用统一代码
     */
    private String beforeLicenseNumber;

    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店
     */
    @ApiModelProperty("erp供应链角色：1-经销商 2-终端医院 3-终端药店")
    private Integer supplyChainRole;

    /**
     * 药品经营许可证编号
     */
    @ApiModelProperty("药品经营许可证编号")
    private String distributionLicenseNumber;

    /**
     * 医机构执业许可证
     */
    @ApiModelProperty("医机构执业许可证")
    private String institutionPracticeLicense;

    /**
     * 所属省份编码
     */
    @ApiModelProperty("所属省份编码")
    private String provinceCode;

    /**
     * 所属省份名称
     */
    @ApiModelProperty("所属省份名称")
    private String provinceName;

    /**
     * 所属城市编码
     */
    @ApiModelProperty("所属城市编码")
    private String cityCode;

    /**
     * 所属城市名称
     */
    @ApiModelProperty("所属城市名称")
    private String cityName;

    /**
     * 所属区域编码
     */
    @ApiModelProperty("所属区域编码")
    private String regionCode;

    /**
     * 所属区域名称
     */
    @ApiModelProperty("所属区域名称")
    private String regionName;

    /**
     * 详细地址
     */
    @ApiModelProperty("详细地址")
    private String address;

    /**
     * 省区
     */
    @ApiModelProperty("省区")
    private String provincialArea;

    /**
     * 电话
     */
    @ApiModelProperty("电话")
    private String phone;

    /**
     * 表单操作:1-新增,2-修改
     */
    @ApiModelProperty("表单操作:1-新增,2-修改")
    private Integer optType;

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

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String notes;

    /**
     * 数据归档：1-开启 2-关闭
     */
    private Integer archiveStatus;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;


}
