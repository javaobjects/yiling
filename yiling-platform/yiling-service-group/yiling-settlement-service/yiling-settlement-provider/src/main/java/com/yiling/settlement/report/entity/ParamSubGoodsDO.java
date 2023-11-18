package com.yiling.settlement.report.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报表子参数商品关联表
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("report_param_sub_goods")
public class ParamSubGoodsDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 参数id
     */
    private Long paramId;

    /**
     * 子参数id
     */
    private Long paramSubId;

    /**
     * 参数类型：1-商品类型 2-促销活动 3-阶梯规则  4-会员返利
     */
    private Integer parType;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 商家eid
     */
    private Long eid;

    /**
     * 商业商品内码
     */
    private String goodsInSn;

    /**
     * 对应以岭的商品id
     */
    private Long ylGoodsId;

    /**
     * 老yl_goods_id
     */
    private Long ylGoodsIdOld;

    /**
     * yl_goods_id最近修改时间
     */
    private Date ylGoodsIdUpdateTime;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品规格
     */
    private String goodsSpecification;

    /**
     * 奖励类型：1-金额 2-百分比
     */
    private Integer rewardType;

    /**
     * 门槛数量（仅参数类型为阶梯时有意义）
     */
    private Integer thresholdCount;

    /**
     * 奖励金额
     */
    private BigDecimal rewardAmount;

    /**
     * 奖励百分比
     */
    private BigDecimal rewardPercentage;

    /**
     * 活动&阶梯的订单来源：0-全部 1-B2B 2-自建平台 3-三方平台及其他渠道订单
     */
    private Integer orderSource;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

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
