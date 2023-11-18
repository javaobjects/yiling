package com.yiling.export.export.bo;

import lombok.Data;

/**
 * @author: hongyang.zhang
 * @data: 2022/10/08
 */
@Data
public class ExportDrugWelfareCouponStatisticsBO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 福利计划名称
     */
    private String drugWelfareName;

    /**
     * 商家名称
     */
    private String ename;

    /**
     * 商家销售人员
     */
    private String sellerUserName;

    /**
     * 服药人姓名
     */
    private String medicineUserName;

    /**
     * 服药人手机号
     */
    private String medicineUserPhone;

    /**
     * 使用进度
     */
    private String useSchedule;

    /**
     * 用户入组id
     */
    private Long joinGroupId;

    /**
     * 入组时间
     */
    private String createTime;

    /**
     * 有效期
     */
    private String validTime;

    /**
     * 福利券详情
     */
    private String drugWelfareCouponInfo;

    /**
     * 券码
     */
    private String couponCode;

    /**
     * 使用状态
     */
    private String couponStatus;

    /**
     * 谁核销的
     */
    private String verificationName;

    /**
     * 激活时间
     */
    private String activeTime;

    /**
     * 核销时间
     */
    private String verifyTime;
}
