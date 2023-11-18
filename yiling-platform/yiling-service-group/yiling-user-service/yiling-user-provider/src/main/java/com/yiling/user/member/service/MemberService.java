package com.yiling.user.member.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.enterprise.entity.EnterpriseDO;
import com.yiling.user.member.dto.CurrentMemberDTO;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.MemberDTO;
import com.yiling.user.member.dto.MemberDetailDTO;
import com.yiling.user.member.dto.MemberOrderDTO;
import com.yiling.user.member.dto.MemberSimpleDTO;
import com.yiling.user.member.dto.request.CreateMemberRequest;
import com.yiling.user.member.dto.request.CurrentMemberForMarketingDTO;
import com.yiling.user.member.dto.request.MemberOrderRequest;
import com.yiling.user.member.dto.request.OpenMemberRequest;
import com.yiling.user.member.dto.request.QueryBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberPromoterRequest;
import com.yiling.user.member.dto.request.UpdateMemberRequest;
import com.yiling.user.member.dto.request.UpdateMemberSortRequest;
import com.yiling.user.member.dto.request.UpdateUserTaskMemberRequest;
import com.yiling.user.member.entity.EnterpriseMemberDO;
import com.yiling.user.member.entity.MemberBuyRecordDO;
import com.yiling.user.member.entity.MemberBuyStageDO;
import com.yiling.user.member.entity.MemberDO;
import com.yiling.user.member.entity.MemberOrderDO;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2021/10/25
 */
public interface MemberService extends BaseService<MemberDO> {

    /**
     * 会员列表
     *
     * @param request
     * @return
     */
    Page<MemberDTO> queryListPage(QueryMemberRequest request);

    /**
     * 停止获得
     *
     * @param id
     * @return
     */
    Boolean stopGet(Long id, Long opUserId);

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
     * 更新会员
     *
     * @param request
     * @return
     */
    Boolean updateMember(UpdateMemberRequest request);

    /**
     * 获取当前用户的会员信息
     *
     * @param currentEid
     * @param memberId
     * @return
     */
    CurrentMemberDTO getCurrentMember(Long currentEid, Long memberId);

    /**
     * 开通/续费会员
     *
     * @param request
     * @return
     */
    Boolean openMember(OpenMemberRequest request);

    /**
     * 查询所有会员
     *
     * @return
     */
    List<MemberSimpleDTO> queryAllList();

    /**
     * 生成会员购买记录
     *
     * @param opUserId
     * @param memberOrderDO
     * @param buyStageDO
     * @param enterpriseDO
     * @param memberName
     * @param source
     * @return
     */
    MemberBuyRecordDO insertMemberBuyRecord(Long opUserId, MemberOrderDO memberOrderDO, MemberBuyStageDO buyStageDO, EnterpriseDO enterpriseDO,
                                            String memberName, Integer source);

    /**
     * 获取更新任务对象
     *
     * @param memberOrderDO
     * @param eid
     * @param enterpriseDO
     * @param opUserId
     * @return
     */
    UpdateUserTaskMemberRequest getUpdateUserTaskMemberRequest(MemberOrderDO memberOrderDO, Long eid, EnterpriseDO enterpriseDO, Long opUserId);

    /**
     * 更新排序
     *
     * @param request
     * @return
     */
    boolean updateSort(UpdateMemberSortRequest request);

    /**
     * 获取会员信息
     *
     * @param currentEid
     * @return
     */
    CurrentMemberForMarketingDTO getCurrentMemberForMarketing(Long currentEid);

    /**
     * 插入企业会员日志记录
     *
     * @param buyRecordDO
     * @param enterpriseMemberDO
     * @param originalStartTime
     * @param originalEndTime
     */
    void insertEnterpriseMemberLog(MemberBuyRecordDO buyRecordDO, EnterpriseMemberDO enterpriseMemberDO, Date originalStartTime, Date originalEndTime);
}
