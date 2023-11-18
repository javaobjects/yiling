package com.yiling.user.enterprise.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业采购关系 DTO
 *
 * @author yuecheng.chen
 * @date 2021-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterprisePurchaseRelationDTO extends BaseDO {

    /**
     * 采购商ID
     */
    private Long buyerEid;

    /**
     * 供应商ID
     */
    private Long sellerEid;

    /**
     * 来源：1-协议生成 2-手动创建
     */
    private Integer source;

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
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String name;

    /**
     * 企业简称
     */
    private String shortName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 采购渠道商ID
     */
    private Long buyerChannelId;

    /**
     * 销售渠道商ID
     */
    private Long sellerChannelId;

    /**
     * 是否已选择
     */
    private Boolean chooseFlag;
}
