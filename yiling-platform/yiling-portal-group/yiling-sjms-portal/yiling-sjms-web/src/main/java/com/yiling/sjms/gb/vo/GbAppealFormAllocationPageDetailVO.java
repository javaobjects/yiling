package com.yiling.sjms.gb.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/18
 */
@Data
public class GbAppealFormAllocationPageDetailVO extends BaseVO {

    /**
     * 团购处理列表Id
     */
    @ApiModelProperty(value = "团购处理列表Id")
    private Long appealFormId;

    /**
     * 团购主表Id
     */
    @ApiModelProperty(value = "团购主表Id")
    private Long gbOrderId;

    /**
     * 关联流向表id
     */
    @ApiModelProperty(value = "")
    private Long gafrId;

    /**
     * 团购表单ID
     */
    @ApiModelProperty(value = "")
    private Long formId;

    /**
     * 团购编号
     */
    @ApiModelProperty(value = "团购编号")
    private String gbNo;

    /**
     * 所属月份
     */
    @ApiModelProperty(value = "所属年月")
    private String matchMonth;

    /**
     * 团购月份
     */
    @ApiModelProperty(value = "团购月份")
    private String gbMonth;

    /**
     * 源流向ID
     */
    @ApiModelProperty(value = "源流向ID")
    private Long flowWashId;

    /**
     * 销售日期
     */
    @ApiModelProperty(value = "销售日期")
    private Date soTime;

    /**
     * 省份代码
     */
    @ApiModelProperty(value = "省份代码")
    private String provinceCode;

    /**
     * 商业编码
     */
    @ApiModelProperty(value = "商业编码")
    private Long crmId;

    /**
     * 商业名称
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
     * 产品(sku)编码
     */
    @ApiModelProperty(value = "标准产品编码")
    private String goodsCode;

    /**
     * 产品品名
     */
    @ApiModelProperty(value = "标准产品名称")
    private String goodsName;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal quantity;

    /**
     * 产品单价
     */
    @ApiModelProperty(value = "产品单价")
    private BigDecimal price;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private BigDecimal totalAmount;

    /**
     * 机构部门ID(用户自己填写部门名称时此id为0)
     */
    @ApiModelProperty(value = "机构部门ID(用户自己填写部门名称时此id为0)")
    private Long orgId;

    /**
     * 机构部门
     */
    @ApiModelProperty(value = "部门")
    private String department;

    /**
     * 机构业务部门ID(用户自己填写业务部门名称时此id为0)
     */
    @ApiModelProperty(value = "机构业务部门ID(用户自己填写业务部门名称时此id为0)")
    private Long businessOrgId;

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
     * 分配类型：1-扣减 2-增加
     */
    @ApiModelProperty(value = "分配类型：1-扣减 2-增加")
    private Integer allocationType;

    /**
     * 流向创建时间
     */
    @ApiModelProperty(value = "流向创建时间")
    private Date flowCreateTime;

    /**
     * 流向业务主键
     */
    @ApiModelProperty(value = "流向业务主键")
    private String flowKey;

}
