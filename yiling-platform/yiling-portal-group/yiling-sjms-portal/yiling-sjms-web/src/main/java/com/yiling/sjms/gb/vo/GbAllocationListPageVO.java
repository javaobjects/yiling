package com.yiling.sjms.gb.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2023/5/23
 */
@Data
public class GbAllocationListPageVO extends BaseVO {

    /**
     * 所属年月
     */
    @ApiModelProperty(value = "所属年月")
    private String matchMonth;

    /**
     * 团购编号
     */
    @ApiModelProperty(value = "团购编号")
    private String gbNo;

    /**
     * 团购月份
     */
    @ApiModelProperty(value = "团购月份")
    private String gbMonth;

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
    private Long customerCrmId;

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
    private Long goodsCode;

    /**
     * 标准产品名称
     */
    @ApiModelProperty(value = "标准产品名称")
    private String goodsName;

    /**
     * 数量
     */
    @ApiModelProperty(value = "")
    private BigDecimal quantity;

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
    @ApiModelProperty(value = "主管姓名")
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
     * 分配类型：1-扣减 2-增加
     */
    @ApiModelProperty(value = "分配类型：1-扣减 2-增加")
    private Integer allocationType;

}
