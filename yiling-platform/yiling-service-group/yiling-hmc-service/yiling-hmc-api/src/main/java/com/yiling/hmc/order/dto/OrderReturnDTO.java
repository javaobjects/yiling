package com.yiling.hmc.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 退货表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderReturnDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 退货单编号
     */
    private String returnNo;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 第三方退单编号
     */
    private String thirdReturnNo;

    /**
     * 药品服务终端id
     */
    private Long eid;

    /**
     * 药品服务终端名称
     */
    private String ename;

    /**
     * 状态：1-待审核/2-已退/3-已取消退单
     */
    private Integer returnStatus;

    /**
     * 申请退款金额
     */
    private BigDecimal returnAmount;

    /**
     * 内容日志
     */
    private String content;

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


}
