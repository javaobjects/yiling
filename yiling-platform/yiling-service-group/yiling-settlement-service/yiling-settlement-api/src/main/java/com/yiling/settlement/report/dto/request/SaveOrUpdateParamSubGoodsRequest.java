package com.yiling.settlement.report.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报表参数-商品类型
 * </p>
 *
 * @author gxl
 * @date 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveOrUpdateParamSubGoodsRequest extends BaseRequest {


    private static final long serialVersionUID = -6154158939268732388L;

    /**
     * id
     */
    private Long id;

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
     * 备注
     */
    private String remark;


}
