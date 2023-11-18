package com.yiling.sales.assistant.task.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

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
public class AccompanyingBillMatchDTO extends BaseDTO {

    private static final long serialVersionUID = -6691516027555931259L;
    /**
     * 单据编号
     */
    private String docCode;

    private Long accompanyingBillId;

    /**
     * 流向核对结果
     */
    private Integer result;

    /**
     * erp核对结果
     */
    private Integer erpResult;

    /**
     * crm核对结果
     */
    private Integer crmResult;

    /**
     * erp流向匹配时间
     */
    private Date erpMatchTime;

    /**
     * crm流向匹配时间
     */
    private Date crmMatchTime;

    /**
     * erp订单号
     */
    private String erpNo;

    /**
     * erp发货日期
     */
    private Date erpDeliveryTime;

    /**
     * crm发货日期
     */
    private Date crmDeliveryTime;

    /**
     * erp发货单位eid
     */
    private Long erpDistributorEid;

    /**
     * erp发货单位
     */
    private String erpDistributorName;

    /**
     * erp收货单位
     */
    private String erpRecvName;

    /**
     * erp收货单位eid
     */
    private Long erpRecvEid;

    /**
     * crm收货单位eid
     */
    private Long crmRecvEid;

    /**
     * crm订单号
     */
    private String crmNo;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 收货企业名称
     */
    private String recvEname;

    /**
     * 发货单位
     */
    private String distributorEname;
    /**
     * 对应商品信息
     */
    private List<AccompanyingBillMatchDetailDTO> erpGoodsList;

    /**
     * 对应商品信息
     */
    private List<AccompanyingBillMatchDetailDTO> crmGoodsList;
}
