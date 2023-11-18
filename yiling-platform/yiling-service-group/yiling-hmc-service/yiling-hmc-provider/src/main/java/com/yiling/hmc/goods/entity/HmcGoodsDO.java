package com.yiling.hmc.goods.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

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
@TableName("hmc_goods")
public class HmcGoodsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * IH C端平台药房id
     */
    private Long ihCPlatformId;

    /**
     * IH 配送id
     */
    private Long ihEid;

    /**
     * IH 配送商商品ID
     */
    private Long ihPharmacyGoodsId;

}
