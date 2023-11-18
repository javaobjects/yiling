package com.yiling.goods.medicine.entity;

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
 * @类名 GoodsSaleVolumeDO
 * @描述
 * @创建时间 2023/5/8
 * @修改人 shichen
 * @修改时间 2023/5/8
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_sale_volume")
public class GoodsSaleVolumeDO extends BaseDO {

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品产品线
     */
    private Long goodsLine;

    /**
     * 标准库id
     */
    private Long standardId;

    /**
     * 规格id
     */
    private Long sellSpecificationsId;

    /**
     * 销量
     */
    private Long volume;

    /**
     * 销售日期
     */
    private Date saleDate;

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
