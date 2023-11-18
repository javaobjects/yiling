package com.yiling.sjms.wash.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhigang.guo
 * @date: 2023/3/2
 */
@Data
public class FlowWashInventoryReportVO extends BaseVO {

    /**
     * ERP库存汇总同步数据主键Id
     */
    @ApiModelProperty(value = "ID")
    private Long flowGoodsBatchWashId;
    /**
     * 年月
     */
    @ApiModelProperty(value = "年月")
    private String soMonth;

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "经销商编码")
    private Long crmId;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "经销商名称")
    private String name;



    @ApiModelProperty(value = "所属区域")
    private String regionName;


    /**
     * 代表工号
     */
    @ApiModelProperty(value = "代表工号")
    private String representativeCode;

    /**
     * 代表姓名
     */
    @ApiModelProperty(value = "代表姓名")
    private String representativeName;

    /**
     * 岗位代码
     */
    @ApiModelProperty(value = "岗位代码")
    private Long postCode;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postName;

    /**
     * 产品(sku)编码
     */
    @ApiModelProperty(value = "产品(sku)编码")
    private Long goodsCode;

    /**
     * 标准商品名称
     */
    @ApiModelProperty(value = "标准商品名称")
    private String goodsName;

    /**
     * 标准产品规格
     */
    @ApiModelProperty(value = "标准产品规格")
    private String specifications;

    /**
     * 批号
     */
    @ApiModelProperty(value = "批号")
    private String batchNo;

    /**
     * 库存数量
     */
    @ApiModelProperty(value = "库存数量")
    private BigDecimal inventoryQuantity;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal totalAmount;

    /**
     * 客户商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    @ApiModelProperty(value = "客户商业级别 (取数据字典)")
    private Integer supplierLevel;

    /**
     * 客户普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议
     */
    @ApiModelProperty(value = "客户普药级别 (取数据字典)")
    private Integer generalMedicineLevel;

    /**
     * 商业属性 1-城市商业、2-县级商业
     */
    @ApiModelProperty(value = "商业属性 (取数据字典)")
    private Integer supplierAttribute;

    /**
     * 是否连锁总部 1是 2否
     */
    @ApiModelProperty(value = "是否连锁总部 (取数据字典)")
    private Integer headChainFlag;

    /**
     * 连锁属性 1-NKA连锁、2-LKA连锁、3-SKA连锁、4-PT连锁
     */
    @ApiModelProperty(value = "连锁属性 (取数据字典)")
    private Integer chainAttribute;

    /**
     * 连锁KA 1是 2否
     */
    @ApiModelProperty(value = "连锁KA (取数据字典)")
    private Integer chainKaFlag;

    /**
     * 客户上级公司名称
     */
    @ApiModelProperty(value = "客户上级公司名称")
    private String parentSupplierName;

    /**
     * 客户上级公司编码
     */
    @ApiModelProperty(value = "客户上级公司编码")
    private String parentSupplierCode;

    /**
     * 客户商业销售类型 1-分销型、2-纯销型、3-综合型、4-托管型、5-连锁型
     */
    @ApiModelProperty(value = "客户商业销售类型 (取数据字典)")
    private Integer supplierSaleType;

    /**
     * 基药商信息 1-基药配送商、2-基药促销商
     */
    @ApiModelProperty(value = "基药商信息 (取数据字典)")
    private Integer baseSupplierInfo;

    /**
     * 首次锁定时间
     */
    @ApiModelProperty(value = "首次锁定时间")
    private Date lockTime;

    /**
     * 最后解锁时间
     */
    @ApiModelProperty(value = "最后解锁时间")
    private Date lastUnLockTime;

}
