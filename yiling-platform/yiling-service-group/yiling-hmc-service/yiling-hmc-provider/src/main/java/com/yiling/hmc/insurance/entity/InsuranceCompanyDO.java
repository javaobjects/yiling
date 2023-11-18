package com.yiling.hmc.insurance.entity;

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
 * 保险公司表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_insurance_company")
public class InsuranceCompanyDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 保险服务商名称
     */
    private String companyName;

    /**
     * 执业许可证号/社会信用统一代码
     */
    private String insuranceNo;

    /**
     * 保险服务商状态 1-启用 2-停用
     */
    private Integer status;

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
     * 联系人姓名
     */
    private String contactName;

    /**
     * 退保客服电话
     */
    private String cancelInsuranceTelephone;

    /**
     * 退保地址
     */
    private String cancelInsuranceAddress;

    /**
     * 续保地址
     */
    private String renewInsuranceAddress;

    /**
     * 互联网问诊地址
     */
    private String internetConsultationUrl;

    /**
     * 代理理赔协议地址
     */
    private String claimProtocolUrl;

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
