package com.yiling.hmc.insurance.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class InsuranceDTO extends BaseDTO {

    /**
     * 保险公司id
     */
    private Long insuranceCompanyId;

    /**
     * 保险名称
     */
    private String insuranceName;

    /**
     * 保险状态 1-启用 2-停用
     */
    private Integer status;

    /**
     * 定额类型季度标识
     */
    private String quarterIdentification;

    /**
     * 定额类型年标识
     */
    private String yearIdentification;

    /**
     * 售卖金额
     */
    private BigDecimal payAmount;

    /**
     * 服务商扣服务费比例
     */
    private BigDecimal serviceRatio;

    /**
     * 售卖地址--h5页面链接
     */
    private String url;

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
}
