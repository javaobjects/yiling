package com.yiling.admin.erp.flow.form;

import java.util.List;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/2/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "查询ERP企业库存流向信息分页参数")
public class QueryFlowGoodsBatchPageForm extends QueryPageListForm {

    /**
     * 商业代码（商家eid）
     */
    @ApiModelProperty(value = "商业代码（商家eid）")
    private Long eid;

    /**
     * 商业名称（商家名称）
     */
    @ApiModelProperty(value = "商业名称（商家名称）")
    private String ename;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品批准文号
     */
    @ApiModelProperty(value = "商品批准文号")
    private String license;

    /**
     * 所属省份编码
     */
    @ApiModelProperty(value = "省份编码")
    private String provinceCode;

    /**
     * 所属城市编码
     */
    @ApiModelProperty(value = "城市编码")
    private String cityCode;

    /**
     * 所属区域编码
     */
    @ApiModelProperty(value = "区域编码")
    private String regionCode;

    /**
     * 商品生产厂家
     */
    @ApiModelProperty(value = "生产厂家")
    private String manufacturer;

    /**
     * 订单来源，多选
     */
    @ApiModelProperty(value = "订单来源，多选")
    private List<String> sourceList;

    /**
     * 企业标签id列表
     */
    @ApiModelProperty(value = "企业标签id列表")
    private List<Long> enterpriseTagIdList;

    /**
     * 商品规格ID
     */
    @ApiModelProperty(value = "商品规格ID")
    private Long sellSpecificationsId;

    /**
     * 有无标准库规格关系：0-无, 1-有
     */
    @ApiModelProperty(value = "有无标准库规格关系：0-无, 1-有")
    private Integer specificationIdFlag;
}
