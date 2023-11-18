package com.yiling.mall.member.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.mall.member.entity.MemberOrderDO;
import com.yiling.user.member.dto.request.MemberOrderRequest;

/**
 * <p>
 * 会员订单表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-20
 */
public interface MemberOrderService extends BaseService<MemberOrderDO> {

    /**
     * 创建会员订单
     *
     * @param request
     * @return
     */
    MemberOrderDO createMemberOrder(MemberOrderRequest request);

}
