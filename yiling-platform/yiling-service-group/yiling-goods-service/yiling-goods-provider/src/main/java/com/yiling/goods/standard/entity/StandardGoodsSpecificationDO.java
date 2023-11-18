package com.yiling.goods.standard.entity;

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
 * 商品规格标准表
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("standard_goods_specification")
public class StandardGoodsSpecificationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 标准商品ID
     */
    private Long standardId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 注册证号
     */
    private String licenseNo;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String sellSpecifications;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * YPID编码
     */
    private String ypidCode;

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
