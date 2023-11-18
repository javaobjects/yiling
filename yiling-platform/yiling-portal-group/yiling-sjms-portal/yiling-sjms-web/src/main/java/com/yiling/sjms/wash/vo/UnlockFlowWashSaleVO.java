package com.yiling.sjms.wash.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 流向销售合并报
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockFlowWashSaleVO extends BaseVO {

    /**
     * 月份(计入)
     */
    @ApiModelProperty(value = "月份")
    private String month;

    /**
     * 年份(计入)
     */
    @ApiModelProperty(value = "年份")
    private String year;

    /**
     * 销售日期
     */
    @ApiModelProperty(value = "销售日期")
    private Date   soTime;

    /**
     * 商业编码
     */
    @ApiModelProperty(value = "商业编码")
    private Long   crmId;


    /**
     * 商业名称（商家名称）
     */
    @ApiModelProperty(value = "商业名称（商家名称）")
    private String ename;

    @ApiModelProperty(value = "标准机构名称")
    private String enterpriseName;

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
     * 原始商品名称
     */
    @ApiModelProperty(value = "原始商品名称")
    private String soGoodsName;

    /**
     * 原始商品规格
     */
    @ApiModelProperty(value = "原始商品规格")
    private String soSpecifications;

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
     * 原始数量
     */
    @ApiModelProperty(value = "原始数量")
    private BigDecimal soQuantity;

    /**
     * 产品单价
     */
    @ApiModelProperty(value = "产品单价")
    private BigDecimal salesPrice;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal soTotalAmount;

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
     * 机构省区
     */
    @ApiModelProperty(value = "机构省区")
    private String provincialArea;

    /**
     * 机构业务省区
     */
    @ApiModelProperty(value = "机构业务省区")
    private String businessProvince;

    /**
     * 机构区办代码
     */
    @ApiModelProperty(value = "机构区办代码")
    private String districtCountyCode;

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
     * 非锁分配备注
     */
    @ApiModelProperty(value = "非锁分配备注")
    private String ruleNotes;

    /**
     * 判断：1-不计入2-公司发货不计入3-漏做客户关系对照商业客户4-不参与计算品种5-当月非锁销量6-商业客户数据计入营销中心7-政府机构
     */
    @ApiModelProperty(value = "判断：1-不计入2-公司发货不计入3-漏做客户关系对照商业客户4-不参与计算品种5-当月非锁销量6-商业客户数据计入营销中心7-政府机构")
    private Integer judgment;

    /**
     * 分配状态：1-未分配2-已分配
     */
    @ApiModelProperty(value = "分配状态：1-未分配2-已分配")
    private Integer distributionStatus;

    /**
     * 分配结果来源 1-规则 2-人工
     */
    @ApiModelProperty(value = "分配结果来源 1-规则 2-人工")
    private Integer distributionSource;

    /**
     * 操作状态：1-未操作2-已操作
     */
    @ApiModelProperty(value = "操作状态：1-未操作2-已操作")
    private Integer operateStatus;

    @ApiModelProperty("操作人")
    private String updateUserName;

    @ApiModelProperty("最后操作时间")
    private Date updateTime;
    @ApiModelProperty("备注")
    private String remark;
    private Long updateUser;

}
