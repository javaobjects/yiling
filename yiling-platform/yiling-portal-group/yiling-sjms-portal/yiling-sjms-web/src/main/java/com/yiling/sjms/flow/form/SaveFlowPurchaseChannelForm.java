package com.yiling.sjms.flow.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 SaveFlowPurchaseChannelForm
 * @描述
 * @创建时间 2023/3/2
 * @修改人 shichen
 * @修改时间 2023/3/2
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveFlowPurchaseChannelForm extends BaseForm {

    @ApiModelProperty(value = "id")
    private Long id;

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private Long crmOrgId;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String orgName;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String province;

    /**
     * 省份代码
     */
    @ApiModelProperty(value = "省份代码")
    private String provinceCode;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String city;

    /**
     * 市代码
     */
    @ApiModelProperty(value = "市代码")
    private String cityCode;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private String region;

    /**
     * 区代码
     */
    @ApiModelProperty(value = "区代码")
    private String regionCode;

    /**
     * 采购渠道机构编码
     */
    @ApiModelProperty(value = "采购渠道机构编码")
    private Long crmPurchaseOrgId;

    /**
     * 采购渠道机构名称
     */
    @ApiModelProperty(value = "采购渠道机构名称")
    private String purchaseOrgName;
}