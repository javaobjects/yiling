package com.yiling.f2b.admin.procrelation.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
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
public class QueryProcRelationPageForm extends QueryPageListForm {

    /**
     * 采购关系编号
     */
    @ApiModelProperty("采购关系编号")
    private String procRelationNumber;

    /**
     * 工业主体eid
     */
    @ApiModelProperty("工业主体eid")
    private Long factoryEid;

    /**
     * 配送商eid
     */
    @ApiModelProperty("配送商eid")
    private Long deliveryEid;

    /**
     * 渠道商eid
     */
    @ApiModelProperty("渠道商eid")
    private Long channelPartnerEid;

    /**
     * 渠道ID
     */
    @ApiModelProperty("渠道ID 0-全部 3-一级商 4-二级商 5-KA用户")
    private Long channelId;

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
}
