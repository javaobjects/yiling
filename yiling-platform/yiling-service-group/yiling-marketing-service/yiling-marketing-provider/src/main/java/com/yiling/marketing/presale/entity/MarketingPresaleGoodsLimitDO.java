package com.yiling.marketing.presale.entity;

import java.math.BigDecimal;
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
 * 预售活动商品表
 * </p>
 *
 * @author shixing.sun
 * @date 2022-10-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("marketing_presale_goods_limit")
public class MarketingPresaleGoodsLimitDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 预售活动id
     */
    private Long marketingStrategyId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * 预售价
     */
    private BigDecimal presaleAmount;

    /**
     * 定金比例
     */
    private BigDecimal depositRatio;

    /**
     * 促销方式：0-无 1-定金膨胀 2-尾款立减
     */
    private Integer presaleType;

    /**
     * 定金膨胀倍数
     */
    private BigDecimal expansionMultiplier;

    /**
     * 尾款立减金额
     */
    private BigDecimal finalPayDiscountAmount;

    /**
     * 每人最小预定量
     */
    private Integer minNum;

    /**
     * 每人最大预定量
     */
    private Integer maxNum;

    /**
     * 合计最大预定量
     */
    private Integer allNum;

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
