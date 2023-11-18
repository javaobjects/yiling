package com.yiling.pricing.goods.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.user.common.util.bean.In;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 PopGoodsLimitPriceDO
 * @描述
 * @创建时间 2023/1/4
 * @修改人 shichen
 * @修改时间 2023/1/4
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pop_goods_limit_price")
public class PopGoodsLimitPriceDO extends BaseDO {
    /**
     * 标准库ID
     */
    private Long standardId;

    /**
     * 标准库售卖规格ID
     */
    private Long  sellSpecificationsId;

    /**
     * 价格上限
     */
    private BigDecimal upperLimitPrice;

    /**
     * 价格下限
     */
    private BigDecimal lowerLimitPrice;

    /**
     * 状态 0：正常
     */
    private Integer status;
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
