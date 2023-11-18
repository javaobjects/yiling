package com.yiling.user.procrelation.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-05-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ProcurementRelationDTO extends BaseDTO {

    private static final long serialVersionUID = 4185769166293029381L;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 版本id全局唯一，用于其他业务关联
     */
    private String versionId;

    /**
     * 采购关系编号
     */
    private String procRelationNumber;

    /**
     * 工业主体eid
     */
    private Long factoryEid;

    /**
     * 工业主体名称
     */
    private String factoryName;

    /**
     * 配送商eid
     */
    private Long deliveryEid;

    /**
     * 配送商名称
     */
    private String deliveryName;

    /**
     * 渠道商eid
     */
    private Long channelPartnerEid;

    /**
     * 渠道商名称
     */
    private String channelPartnerName;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 客户关系表id
     */
    private Long enterpriseCustomerId;

    /**
     * 配送类型：1-工业直配 2-三方配送
     */
    private Integer deliveryType;

    /**
     * 采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期
     */
    private Integer procRelationStatus;

    /**
     * 停用时间
     */
    private Date stopTime;

    /**
     * 停用人
     */
    private Long stopUser;

    /**
     * 执行过期的时间
     */
    private Date expireTime;

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
