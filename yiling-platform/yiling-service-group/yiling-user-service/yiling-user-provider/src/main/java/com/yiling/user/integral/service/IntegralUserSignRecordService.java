package com.yiling.user.integral.service;

import java.util.List;

import com.yiling.user.integral.bo.GenerateUserSignRecordBO;
import com.yiling.user.integral.bo.UserSignDetailBO;
import com.yiling.user.integral.bo.UserSignRecordDetailBO;
import com.yiling.user.integral.dto.IntegralUserSignRecordDTO;
import com.yiling.user.integral.dto.request.QueryUserSignRecordRequest;
import com.yiling.user.integral.dto.request.QueryUserSignRecordTurnPageRequest;
import com.yiling.user.integral.entity.IntegralUserSignRecordDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 用户签到记录表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-10
 */
public interface IntegralUserSignRecordService extends BaseService<IntegralUserSignRecordDO> {

    /**
     * 获取用户积分签到记录
     *
     * @param request
     * @return
     */
    List<IntegralUserSignRecordDTO> getUserIntegralRecord(QueryUserSignRecordRequest request);

    /**
     * 生成用户签到日历数据
     *
     * @param request
     * @return
     */
    List<GenerateUserSignRecordBO> generateUserSignData(QueryUserSignRecordRequest request);

    /**
     * 判断当日是否已经签到
     *
     * @param giveRuleId
     * @param platform
     * @param uid
     * @return
     */
    boolean getTodaySignFlag(Long giveRuleId, Integer platform, Long uid);

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
     * 根据发放记录ID获取签到信息
     *
     * @param giveRecordId
     * @return
     */
    List<UserSignDetailBO> getSignDetailByRecordId(Long giveRecordId);
}
