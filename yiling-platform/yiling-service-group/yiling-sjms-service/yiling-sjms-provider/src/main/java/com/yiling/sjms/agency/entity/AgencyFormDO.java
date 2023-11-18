package com.yiling.sjms.agency.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 机构新增修改表单
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("agency_form")
public class AgencyFormDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 主流程表单id
     */
    private Long formId;

    /**
     * 机构基本信息id
     */
    private Long crmEnterpriseId;

    /**
     * crm系统对应客户名称
     */
    private String name;

    /**
     * 社会信用统一代码
     */
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
    private Integer supplyChainRole;

    /**
     * 药品经营许可证编号
     */
    private String distributionLicenseNumber;

    /**
     * 医机构执业许可证
     */
    private String institutionPracticeLicense;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 省区
     */
    private String provincialArea;

    /**
     * 电话
     */
    private String phone;

    /**
     * 表单操作:1-新增,2-修改
     */
    private Integer optType;

    /**
     * 变更项目 1-机构名称、2-社会统一信用代码、3-电话、4-所属区域、5-地址
     */
    private String changeItem;

    /**
     * 备注
     */
    private String notes;

    /**
     * 数据归档：1-开启 2-关闭
     */
    private Integer archiveStatus;

    /**
     * 是否入库：1-是 2-否
     */
    private Integer enterDatabase;

    /**
     * 入库失败原因
     */
    private String failEnterMessage;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
