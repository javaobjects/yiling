package com.yiling.marketing.payPromotion.entity;

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
 * 支付促销参与记录表
 * </p>
 *
 * @author shixing.sun
 * @date 2023-04-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_pay_promotion_participate")
public class MarketingPayPromotionParticipateDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 支付促销活动id
     */
    private Long marketingPayId;

    /**
     * 买家企业ID
     */
    private Long eid;

    /**
     * 买家企业名称
     */
    private String ename;

    /**
     * 参与时间
     */
    private Date participateTime;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 计算区间id
     */
    private Long ruleId;

    /**
     * 批次号(一个批次号多个订单)
     */
    private String batchNo;

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
