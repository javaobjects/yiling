package com.yiling.user.member.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.member.bo.MemberBuyRecordBO;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.request.CancelBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryMemberBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryMemberListRecordRequest;
import com.yiling.user.member.dto.request.UpdateMemberPromoterRequest;
import com.yiling.user.member.dto.request.UpdateMemberReturnRequest;
import com.yiling.user.member.entity.EnterpriseMemberDO;
import com.yiling.user.member.entity.MemberBuyRecordDO;

/**
 * <p>
 * 会员购买记录 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-15
 */
public interface MemberBuyRecordService extends BaseService<MemberBuyRecordDO> {

    /**
     * 查询会员购买记录分页列表
     *
     * @param request
     * @return
     */
    Page<MemberBuyRecordDTO> queryBuyRecordListPage(QueryBuyRecordRequest request);

    /**
     * 更新会员推广方或推广人
     *
     * @param request
     * @return
     */
    Boolean updateBuyMemberPromoter(UpdateMemberPromoterRequest request);

    /**
     * 获取购买记录详情
     *
     * @param id
     * @return
     */
    MemberBuyRecordDTO getBuyRecordDetail(Long id);

    /**
     * 会员提交退款
     *
     * @param request
     * @return
     */
    boolean submitReturn(UpdateMemberReturnRequest request);

    /**
     * 获取购买记录详情根据订单号
     *
     * @param orderNo
     * @return
     */
    MemberBuyRecordDTO getBuyRecordByOrderNo(String orderNo);

    /**
     * 查询存在开通记录数量，只要开通记录就算，即使是退款的
     *
     * @param eid
     * @param memberId
     * @return
     */
    Integer selectCountRecords(Long eid, Long memberId);

    /**
     * 获取最新一条有效的会员购买记录（过滤掉退款的）
     *
     * @param eid
     * @param memberId
     * @return
     */
    MemberBuyRecordDTO getLastBuyRecord(Long eid, Long memberId);

    /**
     * 根据订单编号获取开通会员且未过期的企业
     *
     * @param orderNoList
     * @return
     */
    List<Long> getEidByOrderNoList(List<String> orderNoList);

    /**
     * 根据推广方ID获取所推广的企业ID
     *
     * @param promoterIdList
     * @return
     */
    Map<Long, List<Long>> getEidByPromoterId(List<Long> promoterIdList);

    /**
     * 根据企业ID获取当前企业正在生效的会员购买记录
     *
     * @param eid
     * @return
     */
    List<MemberBuyRecordDTO> getCurrentValidMemberRecord(Long eid);

    /**
     * 查询指定时间段内购买的会员记录
     *
     * @param request
     * @return
     */
    List<MemberBuyRecordBO> getMemberBuyRecordByDate(QueryMemberBuyRecordRequest request);

    /**
     * 根据企业ID获取每种会员对应的推广方ID
     *
     * @param eid 企业ID
     * @return key为会员ID，value为推广方ID（value为0也表示没有推广方）
     */
    Map<Long, Long> getPromoterByEid(Long eid);

    /**
     * 根据企业ID和会员ID，获取所有购买记录
     *
     * @param eid
     * @return
     */
    List<MemberBuyRecordDTO> getMemberRecordListByEid(Long eid, Long memberId);

    /**
     * 根据企业ID和会员ID，获取有效的会员购买记录（即排除过期、退款的等）
     *
     * @param eid
     * @param memberId
     * @return
     */
    List<MemberBuyRecordDTO> getValidMemberRecordList(Long eid, Long memberId);

    /**
     * 取消导入购买记录
     *
     * @param request
     * @return
     */
    boolean cancelBuyRecord(CancelBuyRecordRequest request);

    /**
     * 重新计算当前企业会员的 会员购买记录的开始时间、结束时间、开通类型
     *
     * @param id
     * @param validMemberRecordList
     * @param enterpriseMember
     */
    void updateBuyRecordTime(Long id, List<MemberBuyRecordDTO> validMemberRecordList, EnterpriseMemberDO enterpriseMember);

    /**
     * 根据指定推广方企业集合查询这些推广方推广的会员
     *
     * @param request
     * @return 购买记录集合
     */
    List<MemberBuyRecordDTO> getBuyRecordListByCond(QueryMemberListRecordRequest request);

    /**
     * 获取所有的推广方ID
     *
     * @return
     */
    List<Long> getAllPromoterId();

}
