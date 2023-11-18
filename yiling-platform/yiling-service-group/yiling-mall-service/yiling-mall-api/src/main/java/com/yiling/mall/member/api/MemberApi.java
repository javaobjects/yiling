package com.yiling.mall.member.api;

import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.request.MemberOrderRequest;

/**
 * 会员 API
 *
 * @author: lun.yu
 * @date: 2021/12/13
 */
public interface MemberApi {

    /**
     * 创建会员订单
     * @param request
     * @return
     */
    MemberOrderDTO createMemberOrder(MemberOrderRequest request);

    /**
     * 会员到期续费提醒
     * @return
     */
    Boolean memberExpirationReminderHandler();

}
