package com.yiling.sjms.wash.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/3/8
 */
@Data
public class QueryFlowGoodsBatchSafePageForm extends QueryPageListForm {

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private Long crmEnterpriseId;

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
