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
public class FlowGoodsBatchDetailWashListVO extends BaseVO {

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
     * 库存日期
     */
    @ApiModelProperty(value = "库存日期")
    private Date gbDetailTime;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "原始商品名称")
    private String goodsName;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "原始商品规格")
    private String gbSpecifications;

    /**
     * 批号
     */
    @ApiModelProperty(value = "批号")
    private String gbBatchNo;

    /**
     * 采购数量
     */
    @ApiModelProperty(value = "采购数量")
    private BigDecimal gbQuantity;

    /**
     * 商品单位
     */
    @ApiModelProperty(value = "单位")
    private String gbUnit;

    /**
     * 价格
     */
    @ApiModelProperty(value = "单价")
    private BigDecimal gbPrice;

    @ApiModelProperty(value = "金额")
    private BigDecimal gbTotalAmount;

    /**
     * 商品生产厂家
     */
    @ApiModelProperty(value = "原始商品生产厂家")
    private String gbManufacturer;

    /**
     * 批准文号
     */
    @ApiModelProperty(value = "原始商品批准文号")
    private String gbLicense;

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
    @ApiModelProperty(value = "清洗结果 字典：flow_data_wash_status 1-未清洗 2-正常 3-疑似重复 4-区间外删除")
    private Integer washStatus;

    /**
     * 生产时间
     */
    @ApiModelProperty(value = "生产时间")
    private String gbProduceTime;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private String gbEndTime;
}
