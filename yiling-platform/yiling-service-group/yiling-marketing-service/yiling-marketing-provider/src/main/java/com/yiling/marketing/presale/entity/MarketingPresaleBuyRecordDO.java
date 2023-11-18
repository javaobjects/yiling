package com.yiling.marketing.presale.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 预售活动购买记录表
 * </p>
 *
 * @author shixing.sun
 * @date 2022-10-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_presale_buy_record")
public class MarketingPresaleBuyRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 订单表主键
     */
    private Long orderId;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 预售活动id
     */
    private Long marketingPresaleId;

    /**
     * 企业id
     */
    private Long eid;

    /**
     * 商品id
     */
    private Long goodsId;

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
