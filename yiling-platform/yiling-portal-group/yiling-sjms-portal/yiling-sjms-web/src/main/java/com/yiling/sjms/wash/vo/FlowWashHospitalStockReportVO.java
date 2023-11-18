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
public class FlowWashHospitalStockReportVO extends BaseVO {

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
     * 产品价格
     */
    @ApiModelProperty(value = "产品价格")
    private BigDecimal price;

    /**
     * 上个月库存数量
     */
    @ApiModelProperty(value = "上个月库存数量")
    private BigDecimal lastMonthStockQty;

    /**
     * 上月库存金额（万元)
     */
    @ApiModelProperty(value = "上月库存金额（万元)")
    private BigDecimal lastMonthStockMoney;

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
     * 商销量（购进）数量
     */
    @ApiModelProperty(value = "商销量（购进）数量")
    private BigDecimal saleQty;

    /**
     * 商销量（购进）金额（万元）
     */
    @ApiModelProperty(value = "商销量（购进）金额（万元）")
    private BigDecimal saleMoney;

    /**
     * 纯销量（销售）数量
     */
    @ApiModelProperty(value = "纯销量（销售）数量")
    private BigDecimal pureSaleQty;

    /**
     * 纯销量(销售)金额(万元)
     */
    @ApiModelProperty(value = "纯销量(销售)金额(万元)")
    private BigDecimal pureSaleMoney;

    /**
     * 本月上报库存数量
     */
    @ApiModelProperty(value = "本月上报库存数量")
    private BigDecimal monthStockQty;

    /**
     * 本月上报库存金额（万元）
     */
    @ApiModelProperty(value = "本月上报库存金额（万元）")
    private BigDecimal monthStockMoney;

    /**
     * 期末库存数量
     */
    @ApiModelProperty(value = "期末库存数量")
    private BigDecimal endQty;

    /**
     * 期末库存金额(万元)
     */
    @ApiModelProperty(value = "期末库存金额(万元)")
    private BigDecimal endMoney;

    /**
     * 机构省份
     */
    @ApiModelProperty(value = "机构省份")
    private String provinceName;

    /**
     * 机构城市
     */
    @ApiModelProperty(value = "机构城市")
    private String cityName;

    /**
     * 机构区县
     */
    @ApiModelProperty(value = "机构区县")
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
     * 医院性质 1公立 2私立 3厂办
     */
    @ApiModelProperty(value = "医院性质 1公立 2私立 3厂办 (取数据字典)")
    private Integer medicalNature;

    /**
     * 医院类型 1-综合医院、2专科医院、3部队医院、4儿童医院、5县级医院、6厂矿职工医院、7中医医院、8社区卫生服务站、9乡镇卫生院、10村卫生室、11妇保院、12疾控中心、13私人终端、14社区卫生服务中心、15高校医院、16其他
     */
    @ApiModelProperty(value = "医院类型 (取数据字典)")
    private Integer medicalType;

    /**
     * 以岭级别
     */
    @ApiModelProperty(value = "以岭级别 (取数据字典)")
    private Integer ylLevel;

    /**
     * 国家等级 1三级甲等、2三级乙等、3三级丙等、4二级甲等、5二级乙等、6二级丙等、7一级甲等、8三级特等、9一级乙等、10一级丙等、11其他
     */
    @ApiModelProperty(value = "国家等级 (取数据字典)")
    private Integer nationalGrade;

    /**
     * 是否基药终端 1是 2否
     */
    @ApiModelProperty(value = "是否基药终端 (取数据字典)")
    private Integer baseMedicineFlag;


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
