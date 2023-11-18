package com.yiling.sjms.wash.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fucheng.bai
 * @date 2023/3/1
 */
@Data
public class FlowPurchaseWashListVO extends BaseVO {

    /**
     * 行业库经销商编码
     */
    @ApiModelProperty(value = "经销商编码")
    private Long crmEnterpriseId;

    /**
     * 月流向清洗任务id
     */
    @ApiModelProperty(value = "月流向清洗任务id")
    private Long fmwtId;

    /**
     * 商业名称（商家名称）
     */
    @ApiModelProperty(value = "经销商名称")
    private String name;

    /**
     * Erp销售订单号
     */
    @ApiModelProperty(value = "销售订单号")
    private String poNo;

    /**
     * 供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String enterpriseName;

    /**
     * 行业库机构编码
     */
    @ApiModelProperty(value = "行业库机构编码")
    private Long crmOrganizationId;

    /**
     * 行业库机构名称
     */
    @ApiModelProperty(value = "行业库机构名称")
    private String crmOrganizationName;

    /**
     * 采购日期
     */
    @ApiModelProperty(value = "采购日期")
    private Date poTime;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "原始商品名称")
    private String goodsName;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "原始商品规格")
    private String poSpecifications;

    /**
     * 批号
     */
    @ApiModelProperty(value = "批号")
    private String poBatchNo;

    /**
     * 采购数量
     */
    @ApiModelProperty(value = "采购数量")
    private BigDecimal poQuantity;

    /**
     * 商品单位
     */
    @ApiModelProperty(value = "单位")
    private String poUnit;

    /**
     * 价格
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal poPrice;

    @ApiModelProperty(value = "金额")
    private BigDecimal poTotalAmount;

    /**
     * 商品生产厂家
     */
    @ApiModelProperty(value = "原始商品生产厂家")
    private String poManufacturer;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "原始商品批准文号")
    private String poLicense;

    /**
     * crm商品编码
     */
    @ApiModelProperty(value = "标准产品编码")
    private Long crmGoodsCode;

    @ApiModelProperty(value = "标准产品名称")
    private String crmGoodsName;

    @ApiModelProperty(value = "标准产品规格")
    private String crmGoodsSpecifications;

    @ApiModelProperty(value = "对照状态 字典：flow_data_wash_mapping_status 1-两者都未匹配 2-商品未匹配 3-客户未匹配 4-匹配成功")
    private Integer mappingStatus;

    /**
     * 清洗结果：1-未清洗 2-正常 3-疑似重复 4-区间外删除
     */
    @ApiModelProperty(value = "清洗结果 字典：flow_data_wash_status  1-未清洗 2-正常 3-疑似重复 4-区间外删除")
    private Integer washStatus;

}
