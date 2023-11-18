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
 * b2b商品表
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("b2b_goods")
public class B2bGoodsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 商品状态 1上架 2下架 3待设置
     */
    private Integer goodsStatus;

    /**
     * 下架原因：1平台下架 2质管下架 3供应商下架
     */
    private Integer outReason;

    /**
     * 商品b2b 产品线状态 0：未启用  1 启用
     */
    private Integer status;

    /**
     * 所属企业
     */
    private Long eid;

    /**
     * 售卖规格ID
     */
    private Long sellSpecificationsId;

    /**
     * 标准库ID
     */
    private Long standardId;

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
