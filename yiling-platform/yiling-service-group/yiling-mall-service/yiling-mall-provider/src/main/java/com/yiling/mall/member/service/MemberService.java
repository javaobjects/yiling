package com.yiling.mall.member.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.member.entity.MemberDO;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2021/12/13
 */
public interface MemberService extends BaseService<MemberDO> {

    /**
     * 会员到期续费提醒
     * @return
     */
    Boolean memberExpirationReminderHandler();

}
