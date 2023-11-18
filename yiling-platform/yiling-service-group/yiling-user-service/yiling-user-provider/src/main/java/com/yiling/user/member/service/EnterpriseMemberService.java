package com.yiling.user.member.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.bo.EnterpriseMemberBO;
import com.yiling.user.member.bo.MemberEnterpriseBO;
import com.yiling.user.member.bo.MemberExpiredBO;
import com.yiling.user.member.dto.EnterpriseMemberDTO;
import com.yiling.user.member.dto.request.QueryEnterpriseMemberRequest;
import com.yiling.user.member.dto.request.UpdateReturnEnterpriseMemberRequest;
import com.yiling.user.member.entity.EnterpriseMemberDO;

/**
 * <p>
 * 企业会员表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
public interface EnterpriseMemberService extends BaseService<EnterpriseMemberDO> {

    /**
     * 获取企业会员
     *
     * @param eid
     * @param memberId
     * @return
     */
    EnterpriseMemberDO getEnterpriseMember(Long eid, Long memberId);

    /**
     * 根据会员ID获取持有会员有效期内的企业
     * @param memberId
     * @return
     */
    List<Long> getEnterpriseByMemberId(Long memberId);

    /**
     * 查询企业会员分页
     * @param request
     * @return
     */
    Page<EnterpriseMemberBO> queryEnterpriseMemberPage(QueryEnterpriseMemberRequest request);

    /**
     * 会员退款更新企业会员
     *
     * @param request
     * @return
     */
    boolean updateReturnEnterpriseMember(UpdateReturnEnterpriseMemberRequest request);

    /**
     * 获取所有为会员的终端企业
     *
     * @return
     */
    List<EnterpriseDTO> getMemberEnterprise();

    /**
     * 根据会员ID集合批量查询对应的企业ID
     *
     * @param memberIdList 会员ID集合
     * @return key为会员ID，value为对应的企业ID集合
     */
    Map<Long, List<Long>> getEidListByMemberId(List<Long> memberIdList);

    /**
     * 获取企业会员详情
     *
     * @param id
     * @return
     */
    EnterpriseMemberBO getDetail(Long id);


    /**
     * 查询当前企业是否是任意会员标识
     *
     * @param eid
     * @return
     */
    boolean getEnterpriseMemberStatus(Long eid);

    /**
     * 根据企业ID获取企业当前的会员信息
     *
     * @param eid 企业ID
     * @return 当前企业所开通且未过期的会员信息
     */
    List<MemberEnterpriseBO> getMemberListByEid(Long eid);

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
