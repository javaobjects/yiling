package com.yiling.sjms.gb.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/17
 */
@Data
public class GbAppealFormFlowWashSaleReportPageDetailVO extends BaseVO {

    /**
     * 流向业务主键
     */
    @ApiModelProperty(value = "流向业务主键key")
    private String flowKey;

    /**
     * 源流向Id
     */
    @ApiModelProperty(value = "ID")
    private Long flowWashId;

    /**
     * 所属月份
     */
    @ApiModelProperty(value = "所属年月")
    private String matchMonth;

    /**
     * 销售日期
     */
    @ApiModelProperty(value = "销售日期")
    private Date soTime;

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
    private String customerCrmId;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String enterpriseName;

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
     * 标准产品编码
     */
    @ApiModelProperty(value = "标准产品编码")
    private String goodsCode;

    /**
     * 标准产品名称
     */
    @ApiModelProperty(value = "标准产品名称")
    private String goodsName;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    /**
     * 已匹配团购数量
     */
    @ApiModelProperty(value = "已匹配团购数量")
    private BigDecimal matchQuantity;

    /**
     * 未匹配团购数量
     */
    @ApiModelProperty(value = "未匹配团购数量")
    private BigDecimal unMatchQuantity;

    /**
     * 机构部门
     */
    @ApiModelProperty(value = "部门")
    private String department;

    /**
     * 机构业务部门
     */
    @ApiModelProperty(value = "业务部门")
    private String businessDepartment;

    /**
     * 机构省区
     */
    @ApiModelProperty(value = "省区")
    private String provincialArea;

    /**
     * 机构业务省区
     */
    @ApiModelProperty(value = "业务省区")
    private String businessProvince;

    /**
     * 机构区办代码
     */
    @ApiModelProperty(value = "区办代码")
    private String districtCountyCode;

    /**
     * 机构区办
     */
    @ApiModelProperty(value = "区办")
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
    private String postCode;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String postName;

    /**
     * 已加入标识：true-已加入 false-未加入
     */
    @ApiModelProperty(value = "已加入标识：true-已加入 false-未加入")
    private Boolean selectFlag;

}
