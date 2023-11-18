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
 * <p>
 * 商品sku
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_sku")
public class GoodsSkuDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 公司Id
     */
    private Long eid;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 包装数量
     */
    private Long packageNumber;

    /**
     * 商品产品线
     */
    private Integer goodsLine;

    /**
     * ERP内码
     */
    private String inSn;

    /**
     * ERP编码
     */
    private String sn;

    /**
     * sku属性扩展字段
     */
    private String extensionField;

    /**
     * 库存ID
     */
    private Long inventoryId;

    /**
     * 状态 0：正常 1：停用 2:隐藏
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
