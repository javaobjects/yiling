package com.yiling.user.integral.service;

import com.yiling.user.integral.dto.request.AddIntegralOrderGiveRequest;
import com.yiling.user.integral.dto.request.UpdateIUserIntegralRequest;
import com.yiling.user.integral.entity.UserIntegralDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 用户积分表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
public interface UserIntegralService extends BaseService<UserIntegralDO> {

    /**
     * 根据uid获取用户积分值
     *
     * @param uid 用户ID
     * @param platform 平台：1-B2B 2-健康管理中心患者端 3-以岭互联网医院医生端 4-药店店员端 5-医药代表端 6-销售助手
     * @return
     */
    Integer getUserIntegralByUid(Long uid, Integer platform);

    /**
     * 获取用户积分对象
     *
     * @param uid
     * @param platform
     * @return
     */
    UserIntegralDO getUserIntegral(Long uid, Integer platform);

    /**
     * 变更用户积分值
     *
     * @param request 添加或扣减用户积分请求对象
     * @return 返回剩余的积分值
     */
    Integer updateIntegral(UpdateIUserIntegralRequest request);

    /**
     * 订单送积分
     *
     * @param request
     * @return
     */
    boolean giveIntegralByOrder(AddIntegralOrderGiveRequest request);

    /**
     * 清零用户积分
     *
     * @param platform
     * @return
     */
    boolean clearIntegral(Integer platform);

    /**
     * 清零定向赠送积分
     *
     * @return
     */
    boolean cleanDirectionalGiveIntegral();
}
