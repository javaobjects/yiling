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
 * @author shichen
 * @类名 GoodsControlDO
 * @描述 管控商品表
 * @创建时间 2022/3/29
 * @修改人 shichen
 * @修改时间 2022/3/29
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_goods_control")
public class GoodsControlDO extends BaseDO {

    private static final long serialVersionUID = 1L;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 注册证号
     */
    private String licenseNo;
    /**
     * 标准库规格id
     */
    private Long sellSpecificationsId;
    /**
     * 标准库商品id
     */
    private Long standardId;
    /**
     * 商品市场价
     */
    private BigDecimal marketPrice;
    /**
     * 参保价
     */
    private BigDecimal insurancePrice;

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
}
