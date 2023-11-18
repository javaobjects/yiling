package com.yiling.sjms.wash.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/3/6
 */
@Data
public class QueryFlowInTransitOrderPageForm extends QueryPageListForm {

    /**
     * 所属月份
     */
    @ApiModelProperty(value = "所属年月")
    private String gbDetailMonth;

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码")
    private Long crmEnterpriseId;

    /**
     * 采购渠道机构编码
     */
    @ApiModelProperty(value = "采购渠道机构编码")
    private Long supplyCrmEnterpriseId;

    /**
     * crm商品编码
     */
    @ApiModelProperty(value = "标准产品编码")
    private Long crmGoodsCode;

    /**
     * crm商品规格
     */
    @ApiModelProperty(value = "标准产品规格")
    private String crmGoodsSpecifications;

    /**
     * 商品批次号
     */
    @ApiModelProperty(value = "批号")
    private String gbBatchNo;

    /**
     * 最后操作时间开始
     */
    @ApiModelProperty(value = "最后操作时间开始")
    private Date opTimeStart;

    /**
     * 最后操作时间结束
     */
    @ApiModelProperty(value = "最后操作时间结束")
    private Date opTimeEnd;

}
