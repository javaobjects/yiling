package com.yiling.hmc.order.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryAdminMarkerOrderPageRequest extends QueryPageListRequest {

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 收货人手机号
     */
    private String mobile;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 下单开始时间
     */
    private Date beginTime;

    /**
     * 下单结束时间
     */
    private Date endTime;

    /**
     * 商家id
     */
    private Long eid;
    /**
     * 药品名称
     */
    private String goodsName;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 药品id集合
     */
    private List<Long> goodsIdList;

    /**
     * 用户id集合
     */
    private List<Long> userList;

    /**
     * IH 处方编号
     */
    private String ihPrescriptionNo;

    /**
     * 支付状态:1-未支付，2-已支付,3-已全部退款
     */
    private Integer paymentStatus;

    /**
     * 处方类型 1：西药 0：中药
     */
    private Integer prescriptionType;
}
