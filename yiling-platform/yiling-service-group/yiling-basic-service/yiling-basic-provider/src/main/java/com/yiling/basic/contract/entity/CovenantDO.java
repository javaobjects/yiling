package com.yiling.basic.contract.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("covenant")
public class CovenantDO extends BaseDO {

    private static final long serialVersionUID = 2758943644594819795L;
    /**
     * 契约锁合同id
     */
    private Long qysContractId;

    /**
     * 契约锁业务分类ID
     */
    private Long qysCategoryId;

    /**
     * 合同名称
     */
    private String subject;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 合同过期时间 格式yyyy-MM-dd
     */
    private String expireTime;

    /**
     * 合同终止时间 格式yyyy-MM-dd
     */
    private String endTime;

    /**
     * 发起方名称
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
     * 接收方名称
     */
    private String receiverName;

    /**
     * 接收方经办人名称
     */
    private String receiverOperator;

    /**
     * 接收方联系方式
     */
    private String receiverContact;

    /**
     * 合同状态
     */
    private String status;

    /**
     * oss文件key
     */
    private String fileKey;

    /**
     * 是否删除：0-否 1-是
     */
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;
}
