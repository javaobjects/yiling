package com.yiling.user.member.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.member.bo.MemberEnterpriseBO;
import com.yiling.user.member.bo.MemberExpiredBO;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.MemberDetailDTO;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.MemberSimpleDTO;
import com.yiling.user.member.dto.request.CreateMemberRequest;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;
import com.yiling.user.member.dto.request.ImportOpenMemberRequest;
import com.yiling.user.member.dto.request.OpenMemberRequest;
import com.yiling.user.member.dto.request.QueryMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberPromoterRequest;
import com.yiling.user.member.dto.request.UpdateMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberSortRequest;

/**
 * 会员 API
 *
 * @author: lun.yu
 * @date: 2021/10/25
 */
public interface MemberApi {

    /**
     * 会员列表分页
     *
     * @param request
     * @return
     */
    Page<MemberDTO> queryListPage(QueryMemberRequest request);

    /**
     * 查询所有会员
     *
     * @return
     */
    List<MemberSimpleDTO> queryAllList();

    /**
     * 根据会员名称模糊查询会员ID
     *
     * @param name
     * @return
     */
    List<Long> listIdsByName(String name);

    /**
     * 根据ID集合查询会员列表
     *
     * @param ids
     * @return
     */
    List<MemberDTO> listByIds(List<Long> ids);

    /**
     * 根据会员ID获取持有会员有效期内的企业
     *
     * @param memberId
     * @return
     */
    List<Long> getEnterpriseByMemberId(Long memberId);

    /**
     * 停止获得
     *
     * @param id
     * @param opUserId
     * @return
     */
    Boolean stopGet(Long id,Long opUserId);

    /**
     * 创建会员
     *
     * @param request
     * @return
     */
    Boolean createMember(CreateMemberRequest request);

    /**
     * 获取会员详情
     *
     * @param id
     * @return
     */
    MemberDetailDTO getMember(Long id);

    /**
     * 获取会员基本信息
     *
     * @param id
     * @return
     */
    MemberDTO getById(Long id);

    /**
     * 更新会员
     *
     * @param request
     * @return
     */
    Boolean updateMember(UpdateMemberRequest request);

    /**
     * 获取当前用户的会员信息
     *
     * @param currentEid 企业ID
     * @param memberId 会员ID
     * @return
     */
    CurrentMemberDTO getCurrentMember(Long currentEid, Long memberId);

    /**
     * 获取当前用户的会员信息 (业务需求变更：即将删除，请修改调用地方，不要再使用此接口)
     *
     * @param currentEid 企业ID
     * @return
     */
    @Deprecated
    CurrentMemberDTO getCurrentMember(Long currentEid);

    /**
     * 回调开通/续费会员
     *
     * @param request
     * @return
     */
    Boolean openMember(OpenMemberRequest request);

    /**
     * 更新会员推广方或推广人
     *
     * @param request
     * @return
     */
    Boolean updateBuyMemberPromoter(UpdateMemberPromoterRequest request);

    /**
     * 导入开通会员
     *
     * @param request
     * @return
     */
    boolean importBuyMember(ImportOpenMemberRequest request);

    /**
     * 根据会员ID集合批量查询对应的企业ID
     *
     * @param memberIdList 会员ID集合
     * @return key为会员ID，value为对应的企业ID集合
     */
    Map<Long, List<Long>> getEidListByMemberId(List<Long> memberIdList);

    /**
     * 根据会员购买条件ID集合批量查询对应的企业ID
     *
     * @param buyStageIdList 会员购买条件ID集合
     * @return key为会员购买条件ID，value为对应的企业ID集合
     */
    Map<Long, List<Long>> getEidListByBuyStageId(List<Long> buyStageIdList);

    /**
     * 根据推广方ID获取所推广的企业ID
     *
     * @param promoterIdList
     * @return
     */
    Map<Long, List<Long>> getEidByPromoterId(List<Long> promoterIdList);

    /**
     * 根据企业ID获取企业当前的会员信息
     *
     * @param eid 企业ID
     * @return 当前企业所开通且未过期的会员信息
     */
    List<MemberEnterpriseBO> getMemberListByEid(Long eid);

    /**
     * 更新排序
     *
     * @param request
     * @return
     */
    boolean updateSort(UpdateMemberSortRequest request);

    /**
     * 获取当前用户的会员信息 (业务需求变更：即将删除，请修改调用地方，不要再使用此接口)
     *
     * @param currentEid 企业ID
     * @return
     */
    CurrentMemberForMarketingDTO getCurrentMemberForMarketing(Long currentEid);
}
