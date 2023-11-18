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
public class FlowWashPharmacyPurchaseReportVO extends BaseVO {

    /**
     * 年月
     */
    @ApiModelProperty(value = "年月")
    private String soMonth;

    /**
     * 机构编码
     */
    @ApiModelProperty(value = "机构编码")
    private Long crmId;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
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
     * 流向内购进数量
     */
    @ApiModelProperty(value = "流向内购进数量")
    private BigDecimal innerPurchaseQty;

    /**
     * 流向外购进窜货批复
     */
    @ApiModelProperty(value = "流向外购进窜货批复")
    private BigDecimal fleeingPurchaseQty;

    /**
     * 流向外购进窜货扣减
     */
    @ApiModelProperty(value = "流向外购进窜货扣减")
    private BigDecimal fleeingPurchaseReduceQty;

    /**
     * 流向外购进反流向
     */
    @ApiModelProperty(value = "流向外购进反流向")
    private BigDecimal purchaseReverse;

    /**
     * 流向外购进合计
     */
    @ApiModelProperty(value = "流向外购进合计")
    private BigDecimal outterPurchaseSum;

    /**
     * 购进数量合计
     */
    @ApiModelProperty(value = "购进数量合计")
    private BigDecimal purchaseSum;

    /**
     * 购进总金额
     */
    @ApiModelProperty(value = "购进总金额")
    private BigDecimal purchaseSumMoney;

    /**
     * 产品价格
     */
    @ApiModelProperty(value = "产品价格")
    private BigDecimal price;

    /**
     * 所属区域
     */
    @ApiModelProperty(value = "所属区域")
    private String regionName;

    /**
     * 省区
     */
    @ApiModelProperty(value = "省区")
    private String provincialArea;

    /**
     * 机构业务省区
     */
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
     * 业务区域（区办）
     */
    @ApiModelProperty(value = "业务区域（区办）")
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
     * 药店属性 1-连锁分店；2-单体药店
     */
    @ApiModelProperty(value = "药店属性 (取数据字典)")
    private Integer pharmacyAttribute;

    /**
     * 药店类型 1-直营；2-加盟
     */
    @ApiModelProperty(value = "药店类型 (取数据字典)")
    private Integer pharmacyType;

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
     * 药店级别 1-A级；2-B级；3-C级
     */
    @ApiModelProperty(value = "药店级别 (取数据字典)")
    private Integer pharmacyLevel;

    /**
     * 连锁属性
     */
    @ApiModelProperty(value = "连锁属性 (取数据字典)")
    private Integer pharmacyChainAttribute;

    /**
     * 标签属性 1-社区店；2-商圈店；3-院边店；4-电商店
     */
    @ApiModelProperty(value = "标签属性 (取数据字典)")
    private Integer labelAttribute;

    /**
     * 是否目标 1-是；2-否
     */
    @ApiModelProperty(value = "是否目标 (取数据字典)")
    private Integer targetFlag;

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
