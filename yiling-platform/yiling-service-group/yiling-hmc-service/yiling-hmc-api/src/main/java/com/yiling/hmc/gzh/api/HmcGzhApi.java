package com.yiling.hmc.gzh.api;


/**
 * HMC 公众号 API
 *
 * @Author fan.shen
 * @Date 2022-09-15
 */
public interface HmcGzhApi {

    /**
     * 判断用户是否关注健康管理中心公众号
     *
     * @param userId 用户ID
     * @return true - 已关注； false - 未关注
     */
    boolean hasUserSubscribeHmcGZH(Long userId);

    /**
     * 创建活动分享图
     *
     * @param userId     - 分享人Id
     * @param activityId - 活动id
     * @param bgUrl      - 活动背景图
     * @return
     */
    String createActivityShareImage(Long userId, Long activityId, String bgUrl);

    /**
     * 创建活动分享图
     *
     * @param userId - 分享人Id
     * @param id     - 活动id
     * @param url    - 活动背景图
     * @return
     */
    String createEvaluateShareImage(Long userId, Long id, String url);
}
