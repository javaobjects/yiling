package com.yiling.order.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2021/8/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryOrderReturnInfoRequest extends QueryPageListRequest {

    /**
     * 退货单来源1-POP 2-销售助手 3-B2B
     */
    private Integer returnSource;

    /**
     * 查询导出来源:1.采购商退货单，2.销售商退货单，3.运营端退货单
     */
    private Integer queryType;
    /**
     * 订单号
     */
    private String  orderNo;
    /**
     * 退货单号
     */
    private String  orderReturnNo;
    /**
     * 供应商名称
     */
    private String  sellerEname;
    /**
     * 采购商名称
     */
    private String  buyerEname;
    /**
     * 开始时间
     */
    private Date    startTime;
    /**
     * 结束时间
     */
    private Date    endTime;

    /**
     * 1-供应商退货单（驳回退货单） 2-破损退货单 3-采购退货单
     */
    private Integer returnType;

    /**
     * 订单状态 1-待审核 2-审核通过 3-审核驳回
     */
    private Integer returnStatus;

    //===============================下面的是更具登录信息获取的，不需要前端传入=========================================

    /**
     * 商务联系人
     */
    private List<Long> userIdList;


    /**
     * 采购商eid
     */
    private Long       eid;
    /**
     * eidlist
     */
    private List<Long> eidList;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 是否是以岭普通人员，需要使用userId查询
     */
    private Boolean yiLingOrdinary;

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 部门Id
     */
    private Long departmentId;

    /**
     * 部门类型:1-万州部门 2-普通部门 3-大运河数拓部门 4-大运河分销部门 5-所有部门
     */
    private Integer departmentType;
}
