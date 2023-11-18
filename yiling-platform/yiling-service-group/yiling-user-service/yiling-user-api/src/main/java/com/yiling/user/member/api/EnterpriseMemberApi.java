package com.yiling.user.member.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.bo.EnterpriseMemberBO;
import com.yiling.user.member.bo.MemberEnterpriseBO;
import com.yiling.user.member.bo.MemberExpiredBO;
import com.yiling.user.member.dto.EnterpriseMemberDTO;
import com.yiling.user.member.dto.MemberEquityDTO;
import com.yiling.user.member.dto.request.CreateMemberEquityRequest;
import com.yiling.user.member.dto.request.QueryEnterpriseMemberRequest;
import com.yiling.user.member.dto.request.QueryMemberEquityRequest;
import com.yiling.user.member.dto.request.UpdateMemberEquityRequest;

/**
 * 企业会员 API
 *
 * @author: lun.yu
 * @date: 2022-09-28
 */
public interface EnterpriseMemberApi {

    /**
     * 分页查询企业会员
     *
     * @param request
     * @return
     */
    Page<EnterpriseMemberBO> queryEnterpriseMemberPage(QueryEnterpriseMemberRequest request);

    /**
     * 获取企业会员详情
     *
     * @param id
     * @return
     */
    EnterpriseMemberBO getDetail(Long id);

    /**
     * 查询当前企业的所有会员信息
     *
     * @param eid
     * @return
     */
    List<MemberEnterpriseBO> getMemberListByEid(Long eid);

    /**
     * 获取所有为会员的终端企业
     *
     * @return
     */
    List<EnterpriseDTO> getMemberEnterprise();

    /**
     * 查询当前企业是否是任意会员标识
     *
     * @param eid
     * @return
     */
    boolean getEnterpriseMemberStatus(Long eid);

    /**
     * 获取企业会员
     *
     * @param eid
     * @param memberId
     * @return
     */
    EnterpriseMemberDTO getEnterpriseMember(Long eid, Long memberId);

    /**
     * 获取会员到期需要提醒列表
     *
     * @param currentEid
     * @param currentUserId
     * @return
     */
    List<MemberExpiredBO> getMemberExpiredList(Long currentEid, Long currentUserId);

    /**
     * 根据会员ID集合获取持有会员有效期内的企业
     *
     * @param memberIdList
     * @return
     */
    List<Long> getEnterpriseByMemberList(List<Long> memberIdList);

    /**
     * 获取企业当前有效的会员
     *
     * @param eid
     * @return
     */
    List<Long> getMemberByEid(Long eid);
}
