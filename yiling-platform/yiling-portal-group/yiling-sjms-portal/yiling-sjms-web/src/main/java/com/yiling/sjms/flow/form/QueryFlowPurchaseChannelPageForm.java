package com.yiling.sjms.flow.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryFlowPurchaseChannelPageForm
 * @描述
 * @创建时间 2023/3/2
 * @修改人 shichen
 * @修改时间 2023/3/2
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowPurchaseChannelPageForm extends QueryPageListForm {

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String orgName;

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private Long crmOrgId;

    /**
     * 省代码
     */
    @ApiModelProperty(value = "省代码")
    private String provinceCode;

    /**
     * 市代码
     */
    @ApiModelProperty(value = "市代码")
    private String cityCode;

    /**
     * 区代码
     */
    @ApiModelProperty(value = "区代码")
    private String regionCode;

    /**
     * 采购渠道机构名称
     */
    @ApiModelProperty(value = "采购渠道机构名称")
    private String purchaseOrgName;

    /**
     * 采购渠道机构编码
     */
    @ApiModelProperty(value = "采购渠道机构编码")
    private Long crmPurchaseOrgId;
}
