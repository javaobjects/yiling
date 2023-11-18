package com.yiling.user.member.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.request.MemberOrderRequest;
import com.yiling.user.member.dto.request.QueryMemberOrderPageRequest;
import com.yiling.user.member.entity.MemberOrderDO;

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
     * 查询会员订单分页列表
     *
     * @param request
     * @return
     */
    Page<MemberOrderDTO> queryMemberOrderPage(QueryMemberOrderPageRequest request);

    /**
     * 获取会员订单根据订单号
     *
     * @param orderNo
     * @return
     */
    MemberOrderDTO getMemberOrderByOrderNo(String orderNo);

    /**
     * 根据订单编号获取订单对应的购买条件ID
     *
     * @param orderNoList
     * @return key 为 orderNo，value 为 stageId
     */
    Map<String, Long> getStageByOrderList(List<String> orderNoList);

    /**
     * 根据订单编号批量获取会员订单
     *
     * @param orderNoList
     * @return
     */
    List<MemberOrderDTO> getMemberOrderByOrderNoList(List<String> orderNoList);

    /**
     * 根据会员购买条件ID批量查询会员订单
     *
     * @param buyStageId
     * @return
     */
    List<MemberOrderDTO> getMemberOrderByStageId(Long buyStageId);
}
