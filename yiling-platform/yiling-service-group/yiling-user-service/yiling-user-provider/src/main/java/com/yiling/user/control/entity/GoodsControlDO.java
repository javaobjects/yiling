package com.yiling.user.control.entity;

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
 * 商品控销表
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_control")
public class GoodsControlDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 商品Id
     */
    private Long goodsId;

    /**
     * 商业公司id
     */
    private Long eid;

    /**
     * 控销类型：1客户控销 2类型控销 3区域控销
     */
    private Integer controlType;

    /**
     * 设置类型：0全部 1部分
     */
    private Integer setType;

    /**
     * 控销描述
     */
    private String controlDescribe;

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
