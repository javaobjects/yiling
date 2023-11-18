package com.yiling.hmc.order.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单操作表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-04-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderOperateDTO extends BaseDTO {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 操作人
     */
    private Long operateUserId;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作功能:1-自提/2-发货/3-退货/4-收货
     */
    private Integer operateType;

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
