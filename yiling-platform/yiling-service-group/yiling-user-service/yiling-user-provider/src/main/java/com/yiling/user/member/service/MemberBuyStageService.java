package com.yiling.user.member.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.request.QueryMemberBuyStagePageRequest;
import com.yiling.user.member.dto.request.QueryMemberBuyStageRequest;
import com.yiling.user.member.entity.MemberBuyStageDO;

/**
 * <p>
 * 会员购买条件 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
public interface MemberBuyStageService extends BaseService<MemberBuyStageDO> {

    /**
     * 根据会员订单号查询购买条件名称
     *
     * @param orderNoList
     * @return
     */
    Map<String, String> getStageNameByOrderNo(List<String> orderNoList);

    /**
     * 查询会员购买条件分页列表
     *
     * @param request
     * @return
     */
    Page<MemberBuyStageDTO> queryMemberBuyStagePage(QueryMemberBuyStagePageRequest request);

    /**
     * 根据会员名称模糊查询会员购买条件
     *
     * @param memberName
     * @return
     */
    List<MemberBuyStageDTO> getStageByMemberName(String memberName);
    /**
     * 根据会员购买条件ID集合批量查询对应的企业ID
     *
     * @param buyStageIdList 会员购买条件ID集合
     * @return key为会员购买条件ID，value为对应的企业ID集合
     */
    Map<Long, List<Long>> getEidListByBuyStageId(List<Long> buyStageIdList);

    /**
     * 根据条件查询购买条件
     *
     * @param request
     * @return
     */
    MemberBuyStageDTO getBuyStageByCond(QueryMemberBuyStageRequest request);
}
