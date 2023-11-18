package com.yiling.sjms.flow.form;

import java.util.Date;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 QueryFlowEnterpriseGoodsMappingPageForm
 * @描述
 * @创建时间 2023/3/2
 * @修改人 shichen
 * @修改时间 2023/3/2
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowEnterpriseGoodsMappingPageForm extends QueryPageListForm {

    /**
     * 流向原始名称
     */
    @ApiModelProperty(value = "流向原始产品名称")
    private String flowGoodsName;

    /**
     * 流向原始规格
     */
    @ApiModelProperty(value = "流向原始产品规格")
    private String flowSpecification;

    /**
     * crm标准商品编码
     */
    @ApiModelProperty(value = "标准商品编码")
    private Long crmGoodsCode;

    /**
     * 标准商品名称
     */
    @ApiModelProperty(value = "标准商品名称")
    private String goodsName;

    /**
     * 经销商名称
     */
    @ApiModelProperty(value = "经销商名称")
    private String enterpriseName;

    /**
     * 经销商编码
     */
    @ApiModelProperty(value = "经销商编码")
    private Long crmEnterpriseId;

    /**
     * 开始更新时间
     */
    @ApiModelProperty(value = "最后操作时间-开始时间")
    private Date startUpdateTime;

    /**
     * 结束更新时间
     */
    @ApiModelProperty(value = "最后操作时间-结束时间")
    private Date endUpdateTime;

    /**
     * 开始最后上传时间
     */
    @ApiModelProperty(value = "最后上传时间-开始时间")
    private Date startLastUploadTime;

    /**
     * 结束最后上传时间
     */
    @ApiModelProperty(value = "最后上传时间-结束时间")
    private Date endLastUploadTime;
}
