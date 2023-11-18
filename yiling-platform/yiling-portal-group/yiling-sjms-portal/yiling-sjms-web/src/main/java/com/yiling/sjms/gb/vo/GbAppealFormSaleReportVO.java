package com.yiling.sjms.gb.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/29
 */
@Data
public class GbAppealFormSaleReportVO extends BaseVO {

    /**
     * 流向业务主键
     */
    @ApiModelProperty(value = "流向业务主键key")
    private String flowKey;

    /**
     * 流向主表Id
     */
    @ApiModelProperty(value = "ID")
    private Long flowSaleWashId;

    /**
     * 销售日期
     */
    @ApiModelProperty(value = "销售日期")
    private Date soTime;

    /**
     * 月份（计入）
     */
    @ApiModelProperty(value = "月份（计入）")
    private String month;

    /**
     * 年份（计入）
     */
    @ApiModelProperty(value = "年份（计入）")
    private String year;

    /**
     * 商业编码
     */
    @ApiModelProperty(value = "商业编码")
    private Long crmId;

    /**
     * 商业名称（商家名称）
     */
    @ApiModelProperty(value = "商业名称")
    private String ename;

    /**
     * 原始客户名称
     */
    @ApiModelProperty(value = "原始客户名称")
    private String originalEnterpriseName;

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private Long customerCrmId;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String enterpriseName;


    /**
     * 产品(sku)编码
     */
    @ApiModelProperty(value = "产品(sku)编码")
    private Long goodsCode;

    /**
     * 产品品名
     */
    @ApiModelProperty(value = "产品品名")
    private String goodsName;

    /**
     * 产品品规
     */
    @ApiModelProperty(value = "产品品规")
    private String goodsSpec;

    /**
     * 最终数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal finalQuantity;

    /**
     * 流向分类 1-正常 2-销量申诉 3-流向补传 4-团购处理
     *  {@link com.yiling.dataflow.wash.enums.FlowClassifyEnum}
     */
    @ApiModelProperty(value = "流向类型 (取数据字典)")
    private Integer flowClassify;

}
