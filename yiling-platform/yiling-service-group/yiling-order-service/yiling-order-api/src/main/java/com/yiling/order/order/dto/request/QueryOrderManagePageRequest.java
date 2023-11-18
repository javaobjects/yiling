package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 审核订单列表Request
 * @author:wei.wang
 * @date:2021/7/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderManagePageRequest extends QueryPageListRequest {

    /**
     * 采购商名称
     */
    private String buyerEname;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 下单开始时间
     */
    private Date startCreateTime;

    /**
     * 下单开始时间
     */
    private Date endCreateTime;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    private Integer paymentMethod;

    /**
     * 审核状态：1-未提交 2-待审核 3-审核通过 4-审核驳回
     */
    private Integer auditStatus;

    /**
     * 买家eid
     */
    private List<Long> buyerEid;

    /**
     * 卖家EID
     */
    private List<Long> sellerEidList;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    private Integer orderType;

    /**
     * 部门类型:1-万州部门 2-普通部门
     */
    private Integer departmentType;

    /**
     *商务联系人
     */
    private List<Long> contacterIds;

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 部门ID
     */
    private Long departmentId;


    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     *所属城市编码
     */
    private String cityCode;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 商务联系人名称
     */
    private String contacterName;

    /**
     * 页面选择的部门id
     */
    private Long departmentIdCode;

}

