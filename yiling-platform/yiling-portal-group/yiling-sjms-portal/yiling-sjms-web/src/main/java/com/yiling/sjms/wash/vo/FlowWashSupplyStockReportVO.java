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
public class FlowWashSupplyStockReportVO extends BaseVO {

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
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String specifications;

    /**
     * 产品价格
     */
    @ApiModelProperty(value = "产品价格")
    private BigDecimal price;

    /**
     * 上月库存数量
     */
    @ApiModelProperty(value = "上月库存数量")
    private BigDecimal lastMonthStockQty;


    /**
     * 本月购进数量
     */
    @ApiModelProperty(value = "本月购进数量")
    private BigDecimal purchaseQty;

    /**
     * 本月销售数量
     */
    @ApiModelProperty(value = "本月销售数量")
    private BigDecimal saleQty;

    /**
     * 本月在途数量
     */
    @ApiModelProperty(value = "本月在途数量")
    private BigDecimal onwayQty;

    /**
     * 本月计算库存数量
     */
    @ApiModelProperty(value = "本月计算库存数量")
    private BigDecimal sumQty;

    /**
     * 本月实际库存数量
     */
    @ApiModelProperty(value = "本月实际库存数量")
    private BigDecimal finalQty;

    /**
     * 库存数量差异
     */
    @ApiModelProperty(value = "库存数量差异")
    private BigDecimal diffQty;

    /**
     * 期末库存数量
     */
    @ApiModelProperty(value = "期末库存数量")
    private BigDecimal endQty;

    /**
     * 上月库存金额（万元)
     */
    @ApiModelProperty(value = "上月库存金额（万元)")
    private BigDecimal lastMonthStockMoney;


    /**
     * 本月购进金额（万元)
     */
    @ApiModelProperty(value = "本月购进金额（万元)")
    private BigDecimal purchaseMoney;

    /**
     * 本月销售价金额（万元)
     */
    @ApiModelProperty(value = "本月销售价金额（万元)")
    private BigDecimal saleMoney;

    /**
     * 本月在途金额（万元)
     */
    @ApiModelProperty(value = "本月在途金额（万元)")
    private BigDecimal onwayMoney;

    /**
     * 本月计算库存金额（万元)
     */
    @ApiModelProperty(value = "本月计算库存金额（万元)")
    private BigDecimal sumMoney;

    /**
     * 本月计算库存金额（万元)
     */
    @ApiModelProperty(value = "本月实际库存金额（万元)")
    private BigDecimal finalMoney;

    /**
     * 库存差异差异（万元)
     */
    @ApiModelProperty(value = "库存差异差异（万元)")
    private BigDecimal diffMoney;

    /**
     * 期末库存金额(万元)
     */
    @ApiModelProperty(value = "期末库存金额(万元)")
    private BigDecimal endMoney;


    @ApiModelProperty(value = "区域")
    private String regionName;


    @ApiModelProperty(value = "省区")
    private String provincialArea;

    @ApiModelProperty(value = "机构业务省区")
    private String businessProvince;

    /**
     * 机构部门
     */
    @ApiModelProperty(value = "机构部门")
    private String department;

    /**
     * 机构业务部门
     */
    @ApiModelProperty(value = "机构业务部门")
    private String businessDepartment;

    /**
     * 机构区办
     */
    @ApiModelProperty(value = "机构区办")
    private String districtCounty;

    /**
     * 主管工号
     */
    @ApiModelProperty(value = "主管工号")
    private String superiorSupervisorCode;

    /**
     * 主管名称
     */
    @ApiModelProperty(value = "主管名称")
    private String superiorSupervisorName;


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
     * 产品组
     */
    @ApiModelProperty(value = "产品组")
    private String productGroup;


    /**
     * 客户商业级别 1一级经销商、2二级经销商、3准一级经销商、4连锁商业、5云仓商业、6未分级经销商
     */
    @ApiModelProperty(value = "客户商业级别 (取数据字典)")
    private Integer supplierLevel;

    /**
     * 商业属性 1-城市商业、2-县级商业
     */
    @ApiModelProperty(value = "商业属性 (取数据字典)")
    private Integer supplierAttribute;

    /**
     * 客户普药级别 1-协议普药一级、2-协议普药二级、3-非协议普药一级、4-非协议普药二级、不签协议
     */
    @ApiModelProperty(value = "客户普药级别 (取数据字典)")
    private Integer generalMedicineLevel;


    /**
     * 商业销售类型 1-分销型、2-纯销型、3-综合型、4-托管型、5-连锁型
     */
    @ApiModelProperty(value = "商业销售类型 (取数据字典)")
    private Integer supplierSaleType;

    /**
     * 基药商信息 1-基药配送商、2-基药促销商
     */
    @ApiModelProperty(value = "基药商信息 (取数据字典)")
    private Integer baseSupplierInfo;


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
     * 首次锁定时间
     */
    @ApiModelProperty(value = "首次锁定时间")
    private Date lockTime;

    /**
     * 最后解锁时间
     */
    @ApiModelProperty(value = "最后解锁时间")
    private Date lastUnLockTime;

    /**
     * 省区经理工号
     */
    @ApiModelProperty(value = "省区经理工号")
    private String provinceManagerCode;

    /**
     * 省区经理编号
     */
    @ApiModelProperty(value = "省区经理工号")
    private String provinceManagerName;



}
