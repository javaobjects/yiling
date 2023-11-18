package com.yiling.f2b.admin.procrelation.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 以岭客户
 *
 * @author: dexi.yao
 * @date: 2021/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ProcRelationPageListItemVO extends BaseVO {

    /**
     * 采购关系编号
     */
    @ApiModelProperty("采购关系编号")
    private String procRelationNumber;

    /**
     * 工业主体eid
     */
    @JsonIgnore
    @ApiModelProperty(value = "工业主体eid",hidden = true)
    private Long factoryEid;

    /**
     * 工业主体名称
     */
    @ApiModelProperty("工业主体名称")
    private String factoryName;

    /**
     * 配送商eid
     */
    @JsonIgnore
    @ApiModelProperty(value = "配送商eid",hidden = true)
    private Long deliveryEid;

    /**
     * 配送商名称
     */
    @ApiModelProperty("配送商名称")
    private String deliveryName;

    /**
     * 渠道商eid
     */
    @JsonIgnore
    @ApiModelProperty(value = "渠道商eid",hidden = true)
    private Long channelPartnerEid;

    /**
     * 渠道商名称
     */
    @ApiModelProperty("渠道商名称")
    private String channelPartnerName;

    /**
     * 渠道商渠道id
     */
    @ApiModelProperty(value = "渠道字典channel_type")
    private Long channelPartnerChannelId;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private Date endTime;

    /**
     * 配送类型：1-工业直配 2-三方配送
     */
    @ApiModelProperty("配送类型：1-工业直配 2-三方配送")
    private Integer deliveryType;

    /**
     * 采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期
     */
    @ApiModelProperty("采购关系状态：1-未开始 2-进行中 3-已停用 4-已过期")
    private Integer procRelationStatus;

    /**
     * 创建人
     */
    @JsonIgnore
    @ApiModelProperty(value = "创建人",hidden = true)
    private Long createUser;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createUserStr;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改人
     */
    @JsonIgnore
    @ApiModelProperty(value = "修改人",hidden = true)
    private Long updateUser;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateUserStr;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

}
