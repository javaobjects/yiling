package com.yiling.hmc.goods.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * C端保险药品商家提成设置表
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class HmcGoodsDTO extends BaseDTO {

    /**
     * 商家id
     */
    private Long eid;

    /**
     * 商家名称
     */
    private String ename;

    /**
     * 保险药品id
     */
    private Long goodsId;

    /**
     * 保险药品名称
     */
    private String goodsName;

    /**
     * 产品线状态 0：未启用，1：启用
     */
    private Integer status;

    /**
     * 标准库商品id
     */
    private Long standardId;

    /**
     * 标准库规格id
     */
    private Long sellSpecificationsId;

    /**
     * 商家售卖金额/盒
     */
    private BigDecimal salePrice;

    /**
     * 给终端结算额/盒
     */
    private BigDecimal terminalSettlePrice;

    /**
     * 商品状态 1上架，2下架
     */
    private Integer goodsStatus;

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
