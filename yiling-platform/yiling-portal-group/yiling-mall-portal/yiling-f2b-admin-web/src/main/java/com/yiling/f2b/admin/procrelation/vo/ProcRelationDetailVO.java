package com.yiling.f2b.admin.procrelation.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2021/7/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ProcRelationDetailVO<T> extends BaseVO {

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
     * 商品列表
     */
    @ApiModelProperty("商品列表")
    List<ProcRelationGoodsVO> goodsList;


}
