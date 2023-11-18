package com.yiling.user.integral.api;

import java.util.List;

import com.yiling.user.integral.bo.GenerateUserSignRecordBO;
import com.yiling.user.integral.bo.UserSignRecordDetailBO;
import com.yiling.user.integral.dto.request.AddIntegralOrderGiveRequest;
import com.yiling.user.integral.dto.request.QueryUserSignRecordRequest;
import com.yiling.user.integral.dto.request.QueryUserSignRecordTurnPageRequest;
import com.yiling.user.integral.dto.request.UpdateIUserIntegralRequest;

/**
 * 用户积分 API
 *
 * @author: lun.yu
 * @date: 2023-01-13
 */
public interface UserIntegralApi {

    /**
     * 根据uid获取用户积分值
     *
     * @param uid 用户ID
     * @param platform 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     * @return
     */
    Integer getUserIntegralByUid(Long uid, Integer platform);

    /**
     * 变更用户积分值
     *
     * @param request 添加或扣减用户积分请求对象（变更类型：1-订单送积分 2-签到送积分 3-参与活动消耗 4-兑换消耗 5-退货扣减 6-过期作废）
     * @return 返回剩余的积分值
     */
    Integer updateIntegral(UpdateIUserIntegralRequest request);

    /**
     * 生成用户签到日历数据
     *
     * @param request
     * @return
     */
    List<GenerateUserSignRecordBO> generateUserSignData(QueryUserSignRecordRequest request);

    /**
     * 用户签到翻页
     *
     * @param request
     * @return
     */
    List<GenerateUserSignRecordBO> userSignRecordTurnPage(QueryUserSignRecordTurnPageRequest request);

    /**
     * 获取签到页详情
     *
     * @param giveRuleId
     * @param platform
     * @param uid
     * @return
     */
    UserSignRecordDetailBO getSignDetail(Long giveRuleId, Integer platform, Long uid);

    /**
     * 积分清零
     *
     * @return
     */
    boolean clearIntegral(Integer platform);

    /**
     * 清零定向赠送积分
     *
     * @return
     */
    boolean cleanDirectionalGiveIntegral();

    /**
     * 订单送积分
     *
     * @param request
     * @return
     */
    boolean giveIntegralByOrder(AddIntegralOrderGiveRequest request);

}
