package com.yiling.dataflow.crm.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author: shuang.zhang
 * @date: 2022/8/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CrmEnterpriseDTO extends BaseDTO {

    /**
     * pop平台编码
     */
    private Long eid;

    /**
     * crm系统对应客户编码
     */
    private String code;

    /**
     * crm系统对应客户名称
     */
    private String name;

    /**
     * 统一信用代码
     */
    private String licenseNumber;

    private String crmNumber;

  //  private String department;

    private String provinceName;

    private String cityName;

    private String regionName;

    private String address;

//    private String province;
//
//    private String flowJobNumber;
//
//    private String flowLiablePerson;
//
//    private String commerceJobNumber;
//
//    private String commerceLiablePerson;
//
//    private String terminalType;

    /**
     * 供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role
     */
    private Integer supplyChainRole;

//    private Integer status;
    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
    /**
     * 药品经营许可证编号
     */
    private String distributionLicenseNumber;

    /**
     * 医机构执业许可证
     */
    private String institutionPracticeLicense;

    /**
     * 以岭编码
     */
    private String ylCode;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 备注
     */
    private String businessRemark;

    /**
     * 曾用名
     */
    private String formerName;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 电话
     */
    private String phone;

    /**
     * 传真
     */
    private String fax;

    /**
     * 邮编
     */
    private String postalCode;

    /**
     * 业务状态 1有效 2失效
     */
    private Integer businessCode;

    /**
     * 机构简称
     */
    private String shortName;

}
