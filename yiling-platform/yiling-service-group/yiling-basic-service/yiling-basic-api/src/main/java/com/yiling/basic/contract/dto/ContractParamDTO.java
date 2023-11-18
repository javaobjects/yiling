package com.yiling.basic.contract.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2022/11/15
 */
@Data
@EqualsAndHashCode
public class ContractParamDTO{


    /**
     * 契约锁模板id（必传）
     */
    private Long qysTemplateId;

    /**
     * 契约锁业务id
     */
    private Long qysCategoryId;

    /**
     * 合同编号
     */
    private String sn;

    /**
     * 合同名称（必传）
     */
    private String subject;

    /**
     * 合同过期时间（必传）
     */
    private String expireTime;

    /**
     * 合同终止时间（必传）
     */
    private String endTime;

    /**
     * 合同发起方名称，默认石家庄以岭药业股份有限公司
     */
    private String tenantName;

    /**
     * 发起方名称，默认石家庄以岭药业股份有限公司
     */
    private String initiatorName;

    /**
     * 发起方经办人名称
     */
    private String initiatorOperator;

    /**
     * 发起方联系方式
     */
    private String initiatorContact;

    /**
     * 接收方(乙方公司)名称（必传）
     */
    private String receiverName;

    /**
     * 接收方经办人名称
     */
    private String receiverOperator;

    /**
     * 接收方联系方式（必传）
     */
    private String receiverContact;


}
