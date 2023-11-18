package com.yiling.user.integral.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存积分兑换商品 Request
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveIntegralExchangeGoodsRequest extends BaseRequest {

    /**
     * ID（修改时必须传入）
     */
    private Long id;

    /**
     * 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     */
    private Integer platform;

    /**
     * 物品ID
     */
    private Long goodsId;

    /**
     * 物品名称
     */
    private String goodsName;

    /**
     * 商品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券
     */
    private Integer goodsType;

    /**
     * 兑换所需积分
     */
    private Integer exchangeUseIntegral;

    /**
     * 可兑换数量
     */
    private Integer canExchangeNum;

    /**
     * 单品兑换限制（份/用户）
     */
    private Integer singleMaxExchange;

    /**
     * 兑换限制生效时间
     */
    private Date exchangeStartTime;

    /**
     * 兑换限制失效时间
     */
    private Date exchangeEndTime;

    /**
     * 是否区分用户身份：1-全部 2-指定会员类型
     */
    private Integer userFlag;

    /**
     * 会员ID集合
     */
    private List<Long> memberIdList;

    /**
     * 有效期生效时间
     */
    private Date validStartTime;

    /**
     * 有效期失效时间
     */
    private Date validEndTime;

    /**
     * 上架状态：1-已上架 2-已下架
     */
    private Integer shelfStatus;

    /**
     * 排序值
     */
    private Integer sort;

}
