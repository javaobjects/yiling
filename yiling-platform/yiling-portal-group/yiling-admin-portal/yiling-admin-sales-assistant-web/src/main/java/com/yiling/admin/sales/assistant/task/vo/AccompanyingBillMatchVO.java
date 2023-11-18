package com.yiling.admin.sales.assistant.task.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 随货同行单匹配流向
 * </p>
 *
 * @author gxl
 * @date 2023-01-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AccompanyingBillMatchVO extends BaseVO {

    /**
     * 单据编号
     */
    @ApiModelProperty(value = "单据编号")
    private String docCode;

    @ApiModelProperty(value = "单据编号")
    private Long accompanyingBillId;

    /**
     * 流向核对结果
     */
    @ApiModelProperty(value = "流向核对结果 accompany_bill_match_status")
    private Integer result;

    /**
     * erp核对结果
     */
    @ApiModelProperty(value = "erp核对结果 1-有 0-无")
    private Integer erpResult;

    /**
     * crm核对结果
     */
    @ApiModelProperty(value = "crm核对结果 1-有 0-无")
    private Integer crmResult;

    /**
     * erp流向匹配时间
     */
    @ApiModelProperty(value = "ERP流向获取时间")
    private Date erpMatchTime;

    /**
     * crm流向匹配时间
     */
    @ApiModelProperty(value = "CRM流向获取时间")
    private Date crmMatchTime;


    /**
     * erp发货日期
     */
    @ApiModelProperty(value = "erp发货日期")
    private Date erpDeliveryTime;

    /**
     * crm发货日期
     */
    @ApiModelProperty(value = "crm发货日期")
    private Date crmDeliveryTime;

    /**
     * 收货企业名称
     */
    @ApiModelProperty(value = "收货企业名称【crm和erp都取这个字段】")
    private String recvEname;

    /**
     * 收货企业名称
     */
    @ApiModelProperty(value = "发货企业名称【crm和erp都取这个字段】")
    private String distributorEname;


    /**
     * 对应商品信息
     */
    @ApiModelProperty(value = "erp商品信息")
    private List<AccompanyingBillMatchDetailVO> erpGoodsList;

    /**
     * 对应商品信息
     */
    @ApiModelProperty(value = "crm商品信息")
    private List<AccompanyingBillMatchDetailVO> crmGoodsList;
}
