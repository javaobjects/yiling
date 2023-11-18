package com.yiling.goods.inventory.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *  库存日志记录表
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_inventory_log")
public class GoodsInventoryLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Long gid;

    /**
     * 商品ID
     */
    private Long inventoryId;

    /**
     * 业务类型：instock-入库 modify-库存修改 frozen-冻结 outstock-出库
     */
    private String businessType;

    /**
     * 业务单号
     */
    private String businessNo;

    /**
     * 变更前数量
     */
    private Long beforeQty;

    /**
     * 变更数量
     */
    private Long changeQty;

    /**
     * 变更后数量
     */
    private Long afterQty;

    /**
     * 冻结变更数量
     */
    private Long changeFrozenQty;

    /**
     * 变更前的冻结数量
     */
    private Long beforeFrozenQty;

    /**
     * 变更后数量
     */
    private Long afterFrozenQty;

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
     * 备注
     */
    private String remark;


}
