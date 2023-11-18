package com.yiling.hmc.remind.context;

import com.yiling.hmc.remind.entity.MedsRemindDO;
import com.yiling.hmc.remind.entity.MedsRemindTimeDO;
import com.yiling.hmc.remind.enums.HmcMedsRemindCreateSourceEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 构建用药提醒上下文
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuildMedsRemindContext {

    /**
     * 用药提醒
     */
    private MedsRemindDO remind;

    /**
     * 时间明细
     */
    private List<MedsRemindTimeDO> remindTimeList;

    /**
     * 创建来源 1-用户发起，2-job发起
     */
    private HmcMedsRemindCreateSourceEnum createSource;


}
