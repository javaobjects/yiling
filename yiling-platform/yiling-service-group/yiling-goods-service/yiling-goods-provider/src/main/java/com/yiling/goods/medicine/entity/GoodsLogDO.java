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
 * 商品日志表
 * </p>
 *
 * @author dexi.yao
 * @date 2021-05-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_log")
public class GoodsLogDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    private Long gid;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 批准文号
     */
    private String licenseNo;

    /**
     * 修改项 1-上下架 2-修改库存 3-修改价格
     */
    private Integer modifyColumn;

    /**
     * 原始值
     */
    private String beforeValue;

    /**
     * 修改以后的值
     */
    private String afterValue;

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
