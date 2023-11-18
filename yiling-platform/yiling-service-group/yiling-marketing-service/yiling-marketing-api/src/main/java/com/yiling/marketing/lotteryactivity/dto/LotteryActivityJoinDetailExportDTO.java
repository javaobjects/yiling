package com.yiling.marketing.lotteryactivity.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 抽奖活动参与明细导出 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class LotteryActivityJoinDetailExportDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 抽奖活动ID
     */
    private Long lotteryActivityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 抽奖时间
     */
    private Date lotteryTime;

    /**
     * 省
     */
    private String enterpriseProvinceName;

    /**
     * 市
     */
    private String enterpriseCityName;

    /**
     * 区
     */
    private String enterpriseRegionName;

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户名称
     */
    private String uname;

    /**
     * 店铺企业名称
     */
    private String shopEname;

    /**
     * 奖品类型：1-真实物品 2-虚拟物品 3-商品优惠券 4-会员优惠券 5-空奖 6-抽奖机会(见字典：lottery_activity_reward_type)
     */
    private Integer rewardType;

    /**
     * 奖品类型名称
     */
    private String rewardTypeName;

    /**
     * 奖品名称
     */
    private String rewardName;

    /**
     * 奖品数量
     */
    private Integer rewardNumber;

    /**
     * 兑付状态：1-已兑付 2-未兑付
     */
    private Integer status;

    /**
     * 兑付状态名称
     */
    private String statusName;

    /**
     * 收货人
     */
    private String contactor;

    /**
     * 联系电话
     */
    private String contactorPhone;

    /**
     * 所属省份编码
     */
    private String provinceCode;

    /**
     * 所属省份名称
     */
    private String provinceName;

    /**
     * 所属城市编码
     */
    private String cityCode;

    /**
     * 所属城市名称
     */
    private String cityName;

    /**
     * 所属区域编码
     */
    private String regionCode;

    /**
     * 所属区域名称
     */
    private String regionName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 详细收货地址
     */
    private String detailAddress;

    /**
     * 快递公司（见字典）
     */
    private String expressCompany;

    /**
     * 快递单号
     */
    private String expressOrderNo;

    /**
     * 快递公司单号
     */
    private String expressCompanyOrderNo;

}
