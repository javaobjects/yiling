package com.yiling.user.member.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.EnterpriseMemberDTO;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.MemberBuyStageDTO;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.MemberDetailDTO;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.MemberSimpleDTO;
import com.yiling.user.member.dto.request.CreateMemberRequest;
import com.yiling.user.member.dto.request.MemberOrderRequest;
import com.yiling.user.member.dto.request.OpenMemberRequest;
import com.yiling.user.member.dto.request.QueryBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryEnterpriseMemberRequest;
import com.yiling.user.member.dto.request.QueryMemberBuyStagePageRequest;
import com.yiling.user.member.dto.request.QueryMemberBuyStageRequest;
import com.yiling.user.member.dto.request.QueryMemberOrderPageRequest;
import com.yiling.user.member.dto.request.QueryMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberPromoterRequest;
import com.yiling.user.member.dto.request.UpdateMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnRequest;

/**
 * 会员购买条件 API
 *
 * @author: lun.yu
 * @date: 2022-08-11
 */
public interface MemberBuyStageApi {

    /**
     * 根据会员订单号查询购买条件名称
     *
     * @param orderNoList
     * @return key 为 订单编号，value 为 会员购买条件名称
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
     * 根据会员购买条件ID查询购买条件信息
     *
     * @param ids
     * @return
     */
    List<MemberBuyStageDTO> listByIds(List<Long> ids);

    /**
     * 根据会员购买条件ID查询购买条件信息
     *
     * @param id
     * @return
     */
    MemberBuyStageDTO getById(Long id);

    /**
     * 根据条件查询购买条件
     *
     * @param request
     * @return
     */
    MemberBuyStageDTO getBuyStageByCond(QueryMemberBuyStageRequest request);

}
