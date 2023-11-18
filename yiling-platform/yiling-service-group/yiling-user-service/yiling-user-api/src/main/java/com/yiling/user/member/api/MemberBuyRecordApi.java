package com.yiling.user.member.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.member.bo.MemberBuyRecordBO;
import com.yiling.user.member.dto.MemberBuyRecordDTO;
import com.yiling.user.member.dto.request.CancelBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryMemberBuyRecordRequest;
import com.yiling.user.member.dto.request.QueryMemberListRecordRequest;

/**
 * 会员购买记录 API
 *
 * @author: lun.yu
 * @date: 2022-10-09
 */
public interface MemberBuyRecordApi {

    /**
     * 获取会员购买记录分页列表
     *
     * @param request
     * @return
     */
    Page<MemberBuyRecordDTO> queryBuyRecordListPage(QueryBuyRecordRequest request);

    /**
     * 获取购买记录详情
     *
     * @param id
     * @return
     */
    MemberBuyRecordDTO getBuyRecodeDetail(Long id);

    /**
     * 根据企业ID获取当前企业正在生效的会员购买记录
     *
     * @param eid
     * @return
     */
    List<MemberBuyRecordDTO> getCurrentValidMemberRecord(Long eid);

    /**
     * 根据订单号获取购买记录
     *
     * @param orderNo
     * @return
     */
    MemberBuyRecordDTO getBuyRecodeByOrderNo(String orderNo);

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
     * 取消导入购买记录
     *
     * @param request
     * @return
     */
    boolean cancelBuyRecord(CancelBuyRecordRequest request);

    /**
     * 根据指定推广方企业集合查询这些推广方推广的会员
     *
     * @param request
     * @return 购买记录集合
     */
    List<MemberBuyRecordDTO> getBuyRecordListByCond(QueryMemberListRecordRequest request);

}
